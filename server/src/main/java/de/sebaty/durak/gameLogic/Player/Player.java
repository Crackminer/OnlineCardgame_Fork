package de.sebaty.durak.gameLogic.Player;

import de.sebaty.durak.gameLogic.Attack;
import de.sebaty.durak.gameLogic.Cards.CardStack;
import de.sebaty.durak.gameLogic.Defend;
import de.sebaty.durak.gameLogic.GameManager;

import java.util.ArrayList;


/**
 * This represents one of the 8 possible players.
 *
 * @version 18.07.2021
 * @author Yann Bernhard &lt;yann.bernhard@stud.uni-hannover.de&gt;
 * @author Sebastian Kiel &lt;sebastian.kiel@stud.uni-hannover.de&gt;
 * @author Patrick Schewe &lt;p.schewe@stud.uni-hannover.de&gt;
 * @author Robert Witteck &lt;robert.witteck@stud.uni-hannover.de&gt;
 */
public class Player
{
    private final int id; //is equal to Client ID

    /**
     * The username of the player
     */
    private String name;
    private final ArrayList<Integer> handCards;

    /**
     * Truth Value if player is attacker
     */
    private boolean isAttacker;

    /**
     * Truth Value if player is defender
     */
    private boolean isDefender;

    /**
     * Truth Value if player is active
     */
    private boolean isActive;

    /**
     * Truth Value if player is a bot
     */
    private boolean isBot;

    /**
     * Truth Value if player has skipped
     */
    private boolean skipped;

    /**
     * The String representation of the last action
     */
    private volatile String lastAction;

    /**
     * The constructor of the player
     *
     * @param id        The id of the player
     * @param name      The username of the player
     */
    public Player(int id, String name)
    {
        this.id = id;
        this.name = name;
        setBot(false);
        resetFlags();
        handCards = new ArrayList<>();
    }

    /**
     * Setter for isBot_
     *
     * @param bot Truth value if player is bot
     */
    public void setBot(boolean bot)
    {
        isBot = bot;
    }

    /**
     * Getter for isBot_
     *
     * @return Truth value if player is bot
     */
    public boolean isBot()
    {
        return isBot;
    }

    /**
     * Getter for lastAction_
     *
     * @return The String representation of the last action
     */
    public String getLastAction()
    {
        return lastAction;
    }

    /**
     * Setter for lastAction_
     *
     * @param lastAction    The String representation of the last action
     */
    public void setLastAction(String lastAction)
    {
        System.out.printf("Action: %s, ID: %d\n", lastAction, id);
        this.lastAction = lastAction;
    }

    /**
     * Getter for id_
     *
     * @return The id of the player
     */
    public int getId()
    {
        return id;
    }

    /**
     * Getter for name_
     *
     * @return The username of the player
     */
    public String getName()
    {
        return name;
    }

    /**
     * Setter for name_
     *
     * @param name  The new username of the player (only accessed by bots)
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Getter for isAttacker_
     *
     * @return Truth value if player is attacker
     */
    public boolean isAttacker()
    {
        return isAttacker;
    }

    /**
     * Setter for isAttacker_
     *
     * @param attacker  Truth value if player is attacker
     */
    public void setAttacker(boolean attacker)
    {
        isAttacker = attacker;
    }

    /**
     * Setter for skipped_
     *
     * @param played    Truth value if player has skipped
     */
    public void setSkipped(boolean played)
    {
        skipped = played;
    }

    /**
     * Getter for skipped_
     *
     * @return  Truth value if player has skipped_
     */
    public boolean hasNotSkipped()
    {
        return !skipped;
    }

    /**
     * Getter for isDefender_
     *
     * @return  Truth value if player is defender
     */
    public boolean isDefender()
    {
        return isDefender;
    }

    /**
     * Setter for isDefender_
     *
     * @param defender Truth value if player is defender
     */
    public void setDefender(boolean defender)
    {
        isDefender = defender;
    }

    /**
     * Getter for isActive_
     *
     * @return  Truth value if player is active
     */
    public boolean isActive()
    {
        return isActive;
    }

    /**
     * Getter for isActive_ as an int
     *
     * @return  Truth value if player is active as an int, 1 if true, 0 if false
     */
    public int isActiveInt_()
    {
        return isActive ? 1 : 0;
    }

    /**
     * Setter for isActive_
     *
     * @param active   Truth value if player is active
     */
    public void setActive(boolean active)
    {
        isActive = active;
    }

    /**
     * Getter for handCards_
     *
     * @return  The Arraylist of handcards of the player
     */
    public ArrayList<Integer> getHandCards()
    {
        return handCards;
    }

    /**
     * Getter for handCards_.size()
     *
     * @return  amount of handcards in a players hand
     */
    public int getAmountOfHandCards()
    {
        return this.handCards.size();
    }

    /**
     * Getter for isAttacker_ as an int
     *
     * @return  Truth value if player is attacker as an int, 1 if true, 0 if false
     */
    public int isAttackerInt_()
    {
        return isAttacker ? 1 : 0;
    }

    /**
     * Getter for isDefender_ as an int
     *
     * @return  Truth value if player is defender as an int, 1 if true, 0 if false
     */
    public int isDefenderInt_()
    {
        if (isDefender)
        {
            return 1;
        }
        return 0;
    }

    /**
     * Resets Attacker, Defender, Active and skipped boolean to false, sets lastAction to 'no action'
     */
    public void resetFlags()
    {
        setAttacker(false);
        setDefender(false);
        setActive(false);
        setSkipped(false);
        setLastAction("no action");
    }

    /**
     * Draws one Card and pops it from the card stack.
     *
     * @param stack the card stack to be drawn and popped from.
     */
    private void drawCard(CardStack stack)
    {
        if (stack == null || stack.remainingCards() == 0)
        {
            System.out.print("Stack was empty when trying to pull from stack\n");
            return;
        }
        int firstCard = stack.getFirstCard();
        handCards.add(firstCard);
        stack.popFirstCard();
    }

    /**
     * Draws cards and pops it from the card stack.
     * @param amount amount to be drawn.
     * @param stack the stack to be drawn and popped from.
     */
    public void drawCards(int amount, CardStack stack)
    {
        for (int i = 0; i < amount; i++)
        {
            drawCard(stack);
        }
    }

    /**
     * Takes all visible cards from the middle
     */
    public void takeCards()
    {
        for (int[] intArr : GameManager.visibleCards)
        {
            for (int card : intArr)
            {
                if (card != -1) handCards.add(card);
            }
        }
        GameManager.visibleCards.clear();
    }

    /**
     * Tries to play a clicked card from the handcards of the player
     *
     * @param card  The id of the card to play
     * @return      Truth value if player was able to play card or not
     */
    public boolean playCard(int card)
    {
        if(!isActive) return false;

        if(isDefender && !isAttacker)
        {
            for (int i = 0; i < handCards.size(); i++)
            {
                if(handCards.get(i) == card)
                {
                    if (Defend.defend(card))
                    {
                        handCards.remove(i);
                        return true;
                    }
                    return false;
                }
            }
            // Should be unreachable
            System.out.println("User tried to use card that is not on hand!");
            return false;
        }

        if(!isDefender && isAttacker)
        {
            for (int i = 0; i < handCards.size(); i++)
            {
                if(handCards.get(i) == card)
                {
                    if (Attack.attack(card))
                    {
                        handCards.remove(i);
                        setSkipped(false);
                        return true;
                    }
                    return false;
                }
            }
            // Should be unreachable
            System.out.println("User tried to use card that is not on hand!");
            return false;
        }
        System.out.println("User is neither Attacker, nor Defender OR Attacker AND Defender and is marked as Active!");
        return false;
    }

    /**
     * The toString method of only a players handcards
     *
     * @return A complete String representation of all cards in the hand of the player
     */
    public String handCardsToString()
    {
        StringBuilder returnString = new StringBuilder();
        for (Integer i: handCards)
        {
            returnString.append(String.format("%s ", i));
        }
        return returnString.toString();
    }

    /**
     * The toString method of the player.
     *
     * @return A complete String representation of all relevant information of the player
     */
    @Override
    public String toString()
    {
        return String.format("ID: %s\tName: %s\tCards in hand: %s", getId(), getName(), handCards.toString());
    }
}
