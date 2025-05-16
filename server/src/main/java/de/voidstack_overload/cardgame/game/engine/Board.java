package de.voidstack_overload.cardgame.game.engine;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.voidstack_overload.cardgame.exceptions.GameOverException;
import de.voidstack_overload.cardgame.game.engine.cards.Card;
import de.voidstack_overload.cardgame.game.engine.cards.CardColor;
import de.voidstack_overload.cardgame.game.lobby.Lobby;
import de.voidstack_overload.cardgame.logging.StandardLogger;
import de.voidstack_overload.cardgame.messages.OutgoingMessageType;
import de.voidstack_overload.cardgame.network.User;
import de.voidstack_overload.cardgame.utility.JsonBuilder;
import de.voidstack_overload.cardgame.utility.ResponseBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Board {
    private static final StandardLogger LOGGER = new StandardLogger();

    private List<Player> playerList;
    public Player getPlayerFromUser(User user) {
        for (Player player : playerList) {
            if(player.equals(user)) return player;
        }
        return null;
    }
    private List<Player> spectatorList;
    private int mainAttackerIndex;
    private Player activePlayer;
    private Player attacker;
    private Player secondAttacker;
    private Player defender;
    private List<Card> drawPile;
    private CardColor trumpColor;
    private Card[][] stacks = new Card[6][2];
    private boolean throwingIn = false;
    private Lobby lobby;

    public Board(List<Player> players, Lobby lobby) {
        resetBoard(players);
        this.lobby = lobby;
    }

    private void setActivePlayer(Player player) {
        this.activePlayer = player;
    }

    private final ScheduledExecutorService botExecutor =
            Executors.newSingleThreadScheduledExecutor(r ->
                    new Thread(r, "bot-logic"));

    private void tryForBotAction(Player player) {
        if (player == null || !player.isBot()) {
            return;
        }

        botExecutor.schedule(() -> {
            try {
                makeMove();
            } catch (GameOverException e) {
                LOGGER.log("Game over.");
                lobby.gameOver();
            } catch (Exception e) {
                LOGGER.error("Bot move failed with error: ", e.getMessage());
            }
        }, 1, TimeUnit.SECONDS);
    }

    public void resetBoard(List<Player> players) {
        LOGGER.log("Resetting board");
        emptyStacks();
        this.drawPile = createDrawPile();
        this.trumpColor = drawPile.getLast().cardColor();
        this.playerList = players;
        drawFirstHands(players);
        this.spectatorList = new ArrayList<>();
        Collections.shuffle(playerList);
        selectPlayerArea(0);
    }

    private void drawFirstHands(List<Player> players) {
        for (Player player : players) {
            player.getHand().clear();
            while (player.getHand().size() < 6) {
                drawCard(player);
            }
        }
    }

    private List<Card> createDrawPile() {
        LOGGER.log("Creating draw pile");
        List<Card> drawPile = new ArrayList<>();
        for(int i = 11; i <= 62; i++) {
                drawPile.add(new Card(CardColor.getColorFromInt(i), (i - 11) % 13));
        }
        Collections.shuffle(drawPile);
        return drawPile;
    }

    public boolean playCard(Card card, Player player) throws GameOverException {
        LOGGER.log(player.getUsername() + " is attempting to play card " + card);
        if (!player.equals(activePlayer) || !player.getHand().contains(card)) {
            LOGGER.log("Player " + player.getUsername() + " is not active or is not having the card they want to play.");
            return false;
        }

        boolean validPlay = false;
        if(activePlayer == attacker) {
            validPlay = attackPlay(card, player);
        } else if (activePlayer == defender) {
            validPlay = defensePlay(card, player);
        }
        if(validPlay) {
            sendGameState();
        }
        return validPlay;
    }

    private boolean attackPlay(Card card, Player player) throws GameOverException {
        LOGGER.log(player.getUsername() + " is attempting to attack with card " + card);
        boolean validPlay = playAttackCard(card);
        if(validPlay) {
            player.getHand().remove(card);
            if(drawPile.isEmpty() && player.getHand().isEmpty()) {
                LOGGER.log("Player " + player.getUsername() + " has played last card. Adding them as spectator.");
                if(playerList.size() > 2) {
                    LOGGER.log("Changing attacker due to original attacker playing their last card.");
                    attacker = secondAttacker;
                    secondAttacker = null;
                    setActivePlayer(attacker);
                }
                playerList.remove(player);
                if(!player.isBot()) spectatorList.add(player);
                if(isGameOver()) {
                    throw new GameOverException();
                }
            }
            if(!throwingIn) {
                LOGGER.log("Switching active player to defender");
                setActivePlayer(defender);
            }
        }
        return validPlay;
    }

    private boolean playAttackCard(Card card) {
        if(stacks[0][0] == null) {
            LOGGER.log("No card was played yet. Any card is valid");
            stacks[0][0] = card;
            return true;
        }
        boolean validPlay = false;
        for(Card[] stack : stacks) {
            if(!validPlay && stack[0] != null && stack[0].cardValue() == card.cardValue()) {
                LOGGER.log("Found a card with equal value");
                validPlay = true;
            } else if(!validPlay && stack[1] != null && stack[1].cardValue() == card.cardValue()) {
                validPlay = true;
                LOGGER.log("Found a card with equal value");
            } else if(validPlay && stack[0] == null) {
                LOGGER.log("Valid play was found. Adding card to stack");
                stack[0] = card;
                return true;
            } else if(stack[0] == null) {
                LOGGER.log("Valid play was not found.");
                return false;
            }
        }
        return false;
    }

    private boolean defensePlay(Card card, Player player) throws GameOverException {
        boolean validPlay = playDefenseCard(card);
        if(validPlay) {
            if(secondAttacker != null && secondAttacker.getSkipped()) {
                LOGGER.log("Resetting skipped flag of second attacker.");
                secondAttacker.setSkipped(false);
            }
            player.getHand().remove(card);
            if(player.getHand().isEmpty()) {
                LOGGER.log("Hand is empty.");
                if(drawPile.isEmpty()) {
                    LOGGER.log("Player " + player.getUsername() + " has played last card. Adding them as spectator.");
                    playerList.remove(player);
                    if(!player.isBot()) spectatorList.add(player);
                    if(isGameOver()) {
                        throw new GameOverException();
                    }
                }
                defenseWon();
            }
            LOGGER.log("Setting active player to attacker");
            setActivePlayer(attacker);
        }
        return validPlay;
    }

    private boolean playDefenseCard(Card card) {
        for(Card[] stack : stacks) {
            if(stack[1] == null) {
                if(card.cardColor() == trumpColor) {
                    LOGGER.log("Playing trump as defense");
                    if(stack[0].cardColor() == trumpColor) {
                        LOGGER.log("Defending against another trump");
                        if(stack[0].cardValue() < card.cardValue()) {
                            LOGGER.log("Value of defense was higher.");
                            stack[1] = card;
                            return true;
                        } else {
                            LOGGER.log("Value of defense was lower.");
                            return false;
                        }
                    } else {
                        LOGGER.log("Defending against non trump. Trump automatically beats.");
                        stack[1] = card;
                        return true;
                    }
                } else {
                    LOGGER.log("Playing non trump as defense");
                    if(stack[0].cardColor() == trumpColor) {
                        LOGGER.log("Defending against a trump. Defense fails.");
                        return false;
                    } else {
                        if(card.cardColor() == stack[0].cardColor() && stack[0].cardValue() < card.cardValue()) {
                            LOGGER.log("Playing same color and value of defense was higher.");
                            stack[1] = card;
                            return true;
                        } else {
                            LOGGER.log("Value of defense was lower or defense played incorrect color.");
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void defenseWon() {
        LOGGER.log("Defense won.");
        redraw();
        selectPlayerArea(mainAttackerIndex + 1);
        emptyStacks();
    }

    public boolean skip(Player player) {
        if (!player.equals(activePlayer)) {
            LOGGER.log("Player " + player.getUsername() + " is not active.");
            return false;
        }

        if(player.equals(attacker)) {
            LOGGER.log("Player " + player.getUsername() + " skipped as attacker.");
            if(playerList.size() > 2 && secondAttacker != null && !secondAttacker.getSkipped()) {
                if(!togglePrimaryAttacker()) throw new RuntimeException("game logic error");
                player.setSkipped(true);
            } else if (throwingIn) {
                defenseLost();
                throwingIn = false;
            } else {
                defenseWon();
            }
            sendGameState();
            return true;
        } else if(player.equals(defender)) {
            throwingIn = true;
            setActivePlayer(attacker);
            sendGameState();
            return true;
        }
        return false;
    }

    private void defenseLost() {
        addStacksToDefenderHand();
        redraw();
        selectPlayerArea(mainAttackerIndex + 2);
        emptyStacks();
    }

    private void addStacksToDefenderHand() {
        for(Card[] stack : stacks) {
            if (stack[0] != null) {
                defender.addCard(stack[0]);
            }
            if (stack[1] != null) {
                defender.addCard(stack[1]);
            }
        }
    }

    private void redraw() {
        while(attacker != null && attacker.getHand().size() < 6) {
            if(drawCard(attacker)) return;
        }
        while (secondAttacker != null && secondAttacker.getHand().size() < 6) {
            if(drawCard(secondAttacker)) return;
        }
        while (defender != null && defender.getHand().size() < 6) {
            if(drawCard(defender)) return;
        }
    }

    private boolean drawCard(Player player) {
        if(drawPile.isEmpty()) return true;
        player.addCard(drawPile.removeFirst());
        return false;
    }

    private void emptyStacks() {
        stacks = new Card[6][2];
    }

    private boolean isGameOver() {
        return playerList.size() < 2;
    }

    private void selectPlayerArea(int startingIndex) {
        if(startingIndex < 0) {
            startingIndex = 0;
        } else if(startingIndex >= playerList.size()) {
            startingIndex = startingIndex % playerList.size();
        }
        mainAttackerIndex = startingIndex;
        setActivePlayer(playerList.get(startingIndex));
        attacker = playerList.get(startingIndex);
        defender = playerList.get(startingIndex + 1 < playerList.size() ? startingIndex + 1 : startingIndex + 1 - playerList.size());
        if(playerList.size() > 2) {
            secondAttacker = playerList.get(startingIndex + 2 < playerList.size() ? startingIndex + 2 : startingIndex + 2 - playerList.size());
        } else {
            secondAttacker = null;
        }
    }

    private void makeMove() throws GameOverException {
        if(!activePlayer.isBot()) {
            return;
        }
        ArrayList<Card> possibleActions;
        ArrayList<Card> botHandCards = new ArrayList<>(activePlayer.getHand());

        if(activePlayer == defender) {
            possibleActions = getBotDefenseActions(botHandCards);
        } else {
            // defense lostt so we can throw in cards as attacker and the last slot already has card thrown in
            if (this.throwingIn && this.stacks[5][0] != null)
            {
                possibleActions = new ArrayList<>();
            }
            else
            {
                possibleActions = getBotAttackActions(botHandCards);
            }
        }
        // If no card can be played, skip instead
        if(possibleActions.isEmpty()) {
            skip(activePlayer);
            return;
        }
        //Filter out trump cards to not waste them
        ArrayList<Card> trumps = new ArrayList<>();
        for (Card possible : possibleActions) {
            if (possible.cardColor() == trumpColor) {
                trumps.add(possible);
            }
        }
        if (trumps.size() < possibleActions.size()) {
            for (Card trump : trumps) {
                possibleActions.remove(trump);
            }
        }
        //Trying to sort all cards now from lowest to highest
        Collections.sort(possibleActions);
        playCard(possibleActions.getFirst(), activePlayer);
    }

    private ArrayList<Card> getBotAttackActions(ArrayList<Card> botHandCards) {
        if (stacks[0][0] == null) {
            return botHandCards;
        }
        return getCardsWithSameValueAsStacks(botHandCards);
    }

    private ArrayList<Card> getBotDefenseActions(ArrayList<Card> botHandCards) throws GameOverException {
        ArrayList<Card> possibleActions = new ArrayList<>();

        ArrayList<Card> sameWorth = getCardsWithSameValueAsStacks(botHandCards);

        //Now getting Same Worth cards that are playable
        Card attackingCard = null;
        for(Card[] stack : stacks) {
            if(stack[1] == null) {
                attackingCard = stack[0];
                break;
            }
        }
        for (Card card : botHandCards) {
            assert attackingCard != null;
            if(card.cardColor() == attackingCard.cardColor()) {
                if(card.cardValue() > attackingCard.cardValue()) {
                    possibleActions.add(card);
                }
            } else if (card.cardColor() == trumpColor) {
                possibleActions.add(card);
            }
        }
        ArrayList<Card> bestCards = new ArrayList<>();
        for (Card card : possibleActions) {
            if(sameWorth.contains(card)) {
                bestCards.add(card);
            }
        }
        if(!bestCards.isEmpty()) {
            return bestCards;
        }
        return possibleActions;
    }

    private ArrayList<Card> getCardsWithSameValueAsStacks(ArrayList<Card> botHandCards) {
        ArrayList<Card> sameWorth = new ArrayList<>();
        //getting all playable cards from the hand-cards
        for (Card card : botHandCards) {
            for (Card[] visible : stacks) {
                if (!sameWorth.contains(card)) {
                    if(visible[0] != null) {
                        if (card.cardValue() == visible[0].cardValue()) {
                            sameWorth.add(card);
                            continue;
                        }
                    } else {
                        break;
                    }
                    if (visible[1] != null) {
                        if(card.cardValue() == visible[1].cardValue()) {
                            sameWorth.add(card);
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        return sameWorth;
    }

    private boolean togglePrimaryAttacker() {
        if(secondAttacker == null) {
            return false;
        }
        Player temp = secondAttacker;
        secondAttacker = attacker;
        attacker = temp;
        setActivePlayer(attacker);
        return true;
    }

    public void sendGameState() {
        for(Player player : playerList) {
            if(player.isBot()) continue;
            JsonBuilder gameState = toJson();
            JsonArray playerHandJson = new JsonArray();
            for(Card card : player.getHand()) {
                playerHandJson.add(card.cardToInt());
            }
            gameState.add("hand", playerHandJson);
            player.getWebSocket().send(ResponseBuilder.build(OutgoingMessageType.GAME_STATE, gameState).response());
        }
        for(Player player : spectatorList) {
            player.getWebSocket().send(ResponseBuilder.build(OutgoingMessageType.GAME_STATE, toJson()).response());
        }
        tryForBotAction(activePlayer);
    }

    private JsonBuilder toJson() {
        JsonBuilder json = new JsonBuilder();
        json.add("activePlayer", activePlayer.getUsername());
        json.add("attackers", getAttackerJsonArray());
        json.add("defender", defender.getUsername());
        json.add("players", getPlayersJsonArray());
        json.add("drawPileHeight", drawPile.size());
        json.add("trumpColor", this.trumpColor.getColorAsInt());
        json.add("cardStacks", getStacksJsonArray());
        return json;
    }
    private JsonArray getAttackerJsonArray() {
        JsonArray attackersJsonArray = new JsonArray();
        attackersJsonArray.add(attacker.getUsername());
        if(this.secondAttacker != null) {
            attackersJsonArray.add(secondAttacker.getUsername());
        }
        return attackersJsonArray;
    }
    private JsonArray getPlayersJsonArray() {
        JsonArray players = new JsonArray();
        for(Player p : playerList) {
            JsonObject player = new JsonObject();
            player.addProperty("playerName", p.getUsername());
            player.addProperty("handSize", p.getHand().size());
            players.add(player);
        }
        return players;
    }
    private JsonArray getStacksJsonArray() {
        JsonArray stacksJson = new JsonArray();
        for(Card[] stack : this.stacks) {
            JsonArray stackJson = new JsonArray();
            if(stack[0] != null) {
                stackJson.add(stack[0].cardToInt());
                if(stack[1] != null) {
                    stackJson.add(stack[1].cardToInt());
                } else {
                    stackJson.add(-1);
                }
            } else {
                stackJson.add(-1);
                stackJson.add(-1);
            }
            stacksJson.add(stackJson);
        }
        return stacksJson;
    }

    public void sendCleanGameState() {
        JsonBuilder json = new JsonBuilder();
        json.add("activePlayer", "");
        JsonArray attackersJson = new JsonArray();
        attackersJson.add("");
        json.add("attackers", attackersJson);
        json.add("defender", "");
        json.add("players", new JsonArray());
        json.add("drawPileHeight", 0);
        json.add("trumpColor", -1);
        JsonArray stacksJson = new JsonArray();
        for(Card[] stack : this.stacks) {
            JsonArray stackJson = new JsonArray();
            stackJson.add(-1);
            stackJson.add(-1);
            stacksJson.add(stackJson);
        }
        json.add("cardStacks", stacksJson);

        for(Player player : playerList) {
            if(player.isBot()) continue;
            player.getWebSocket().send(ResponseBuilder.build(OutgoingMessageType.GAME_STATE, json).response());
        }
        for(Player player : spectatorList) {
            player.getWebSocket().send(ResponseBuilder.build(OutgoingMessageType.GAME_STATE, json).response());
        }
    }

    public User getLastPlayer() {
        return playerList.getLast();
    }
}
