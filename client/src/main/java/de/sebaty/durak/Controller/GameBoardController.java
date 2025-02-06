package de.sebaty.durak.Controller;

import de.sebaty.durak.gameLogic.Cards.*;
import de.sebaty.durak.ControllerInterface;
import de.sebaty.durak.Network.ClientNetwork;
import de.sebaty.durak.PaneResizer;
import de.sebaty.durak.fxmlNavigator;
import de.sebaty.durak.gameClient;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Controls the GameBoard and with that the heart components of the clients part in the game
 *
 * @version 18.07.2021
 * @author Yann Bernhard &lt;yann.bernhard@stud.uni-hannover.de&gt;
 * @author Sebastian Kiel &lt;sebastian.kiel@stud.uni-hannover.de&gt;
 * @author Patrick Schewe &lt;p.schewe@stud.uni-hannover.de&gt;
 * @author Robert Witteck &lt;robert.witteck@stud.uni-hannover.de&gt;
 */
public class GameBoardController implements ControllerInterface
{
    /**
     * The Draw pile counter.
     */
    @FXML
    public Label DrawPileCounter;

    /**
     * This is the main pain that contains every other node and is the onlything that gets rescaled
     */
    @FXML
    private Pane GameBoard;

    /**
     * The card draw pile image.
     */
    @FXML
    private ImageView leftoverDeck;

    private final ArrayList<Node> addedNodesArrayList = new ArrayList<>();

    private final ArrayList<Node> addedHandCards = new ArrayList<>();

    private ParsedServerMessage originalMessage;

    /**
     * Controls whether to kill the client on error or disconnect.
     */
    boolean killClientNetworkHandler;

    /**
     * Instantiates the gameboard pane and the draw pile of cards Pane and counter.
     */
    @Override
    public void init()
    {
        Stage stage = gameClient.stage;
        Scene scene = stage.getScene();

        leftoverDeck = (ImageView) scene.lookup("#leftoverDeck");
        Image image = Cards.getSpecialImage(SpecialTexture.BackLowsat);
        leftoverDeck.setImage(image);

        PaneResizer.resizePane(scene.getHeight(), true);

        killClientNetworkHandler = false;
        networkHandler task = new networkHandler();
        new Thread(task).start();
    }

    /**
     * Disconnect client from server and kill client socket. Afterwards sends user to home screen.
     */
    public void disconnect()
    {
        if (ClientNetwork.isLoggedIn_())
        {
            System.out.println("Shutdown client");
            ClientNetwork.sendMessage("disconnect");
            killClientNetworkHandler = true;
        }
        ClientNetwork.stopConnection();
        fxmlNavigator.loadFxml(fxmlNavigator.HOME);
    }

    /**
     * Sends a string containing the clicked card to the server.
     *
     * @param nr    the id of the card
     */
    public void cardClicked(int nr)
    {
        ClientNetwork.sendMessage(String.format("%d", nr));
    }

    /**
     * Sends pass command to the server if pass button is clicked.
     */
    public void pass()
    {
        ClientNetwork.sendMessage("pass");
    }

    /**
     * Sends take command to the server if take button is clicked.
     */
    public void take()
    {
        ClientNetwork.sendMessage("take");
    }

    /**
     * Execute a message/command received from the server. If the message is the game state, this message is parsed to
     * redraw the current state on the game board
     *
     * @param line      command line to execute, that was gotten from the clientsockets bufferIn.
     */
    public void executeLine(String line)
    {
        System.out.println("Message from server:" + line);
        if (line.equals("connection_stopped"))
        {
            return;
        }
        if (line.equals("error"))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Network Error");
            alert.setHeaderText("Server did not answer");
            alert.setContentText("Please try again");
            alert.setResizable(true);
            alert.onShownProperty().addListener(ebox -> Platform.runLater(() -> alert.setResizable(false)));
            alert.showAndWait();
            return;
        }
        if (line.equals("disconnect"))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Message from Server");
            alert.setHeaderText("Server Disconnected");
            alert.setContentText("Please try to reconnect");
            alert.setResizable(true);
            alert.onShownProperty().addListener(ebox -> Platform.runLater(() -> alert.setResizable(false)));
            alert.showAndWait();
            return;
        }
        if (line.equals("GameEnded"))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Message from Server");
            alert.setHeaderText("Game Ended");
            alert.setContentText("The Game has ended and you will automatically be disconnected.");
            alert.setResizable(true);
            alert.onShownProperty().addListener(ebox -> Platform.runLater(() -> alert.setResizable(false)));
            alert.showAndWait();
            fxmlNavigator.loadFxml(fxmlNavigator.HOME);
            return;
        }
        ParsedServerMessage parsedServerMessage = new ParsedServerMessage(line);
        draw(parsedServerMessage);
        PaneResizer.resizePane(gameClient.stage.getScene().getHeight(), true);
    }


    /**
     * Draw the current game state on the game board.
     * Expects the message String to be a valid Gamestate
     *
     * @param parsedServerMessage   the parsed server message with the game state.
     */
    public void draw(ParsedServerMessage parsedServerMessage)
    {
        undrawOldNodes();

        originalMessage = parsedServerMessage;

        //CARDSTACKCOUNT
        String drawPileText = String.format("%d", parsedServerMessage.getDrawPileHeight_());
        DrawPileCounter.setText(drawPileText);

        //TRUMP
        drawTrump(parsedServerMessage.getTrumpColor_());

        //PLAYERLIST
        int playerStart = 60;
        int playerEnd = 460;
        int playerVerticalTop = 5;
        int playerVerticalBottom = playerVerticalTop + 40;

        int playerCount = parsedServerMessage.getPlayers_().size();

        int playerSpace = (playerEnd - playerStart) / ((playerCount + 1) / 2);

        for (int i = 0; i < playerCount; i++)
        {
            int x;
            int y;
            if (i < (playerCount + 1) / 2)
            {
                x = playerStart + i * playerSpace;
                y = playerVerticalTop;
            }
            else
            {
                x = playerStart + (i - (playerCount + 1) / 2) * playerSpace;
                y = playerVerticalBottom;
            }

            drawPlayer(parsedServerMessage.getPlayers_().get(i), x, y, (playerSpace * 0.95));
        }

        //VISIBLECARDS
        int visibleCardsHorizontalSpacing = 80;
        int visibleCardsVerticalTop = 90;
        int visibleCardsVerticalBottom = visibleCardsVerticalTop + 110;

        int visibleCardsCount = parsedServerMessage.getVisibleCards_().size();
        for (int i = 0; i < visibleCardsCount; i++)
        {
            int[] arr = parsedServerMessage.getVisibleCards_().get(i);
            int x = 140 + (i % 3) * visibleCardsHorizontalSpacing;
            drawCard(arr[0], x, (i < 3) ? visibleCardsVerticalTop : visibleCardsVerticalBottom, false);
            if (arr[1] >= 11)
            {
                drawCard(arr[1], x, (i < 3) ? visibleCardsVerticalTop + 15 : visibleCardsVerticalBottom + 15, false);
            }
        }

        //HANDCARDS
        int handStart = 120;
        int handEnd = 480;
        int cardSize = 55;
        int handSpace = handEnd - handStart - cardSize;

        int handSize = parsedServerMessage.getHandCards_().size();
        for (int i = 0; i < handSize; i++)
        {
            int x = handStart + i * (handSpace / handSize);
            int y = 315;
            drawCard(parsedServerMessage.getHandCards_().get(i), x, y, true);
        }

        // WAS SUCCESSFUL
        if (!parsedServerMessage.getWasSuccessful_())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Action");
            alert.setHeaderText("You tried to take an invalid action");
            alert.setContentText("Its either not your turn or the action you tried to do\nis not acceptable by the rules of the game\n");
            alert.setResizable(true);
            alert.onShownProperty().addListener(ebox -> Platform.runLater(() -> alert.setResizable(false)));
            alert.showAndWait();
        }
    }


    /**
     * Clean game board from old game state i.e. remove all cards, player states etc.
     */
    private void undrawOldNodes()
    {
        System.out.printf("Memory: %d\n", Runtime.getRuntime().freeMemory());
        for (Node n : addedNodesArrayList)
        {
            GameBoard.getChildren().remove(n);
        }
        addedNodesArrayList.clear();
        addedHandCards.clear();
    }


    /**
     * Draws the player state i.e. hand cards, attack/defender state etc.
     *
     * @param player                     the player state obtained from the server.
     * @param x                          x-position of the player label
     * @param y                          y-position of the player label
     * @param width                      of the player label
     */
    private void drawPlayer(ParsedServerMessage.Player player, int x, int y, double width)
    {
        int cards = player.getHandCardAmount_();
        String name = player.getName_();

        String playerString = String.format(" %d    %s ", cards, name);

        Label playerLable = new Label();
        playerLable.setLayoutX(x);
        playerLable.setLayoutY(y);
        playerLable.setPrefWidth(width);
        playerLable.setText(playerString);
        Image image;
        if (player.isAttacker_())
        {
            image = Cards.getSpecialImage(SpecialTexture.SwordIcon);
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitHeight(30);
            playerLable.setGraphic(imageView);

        }
        else if (player.isDefender_())
        {
            image = Cards.getSpecialImage(SpecialTexture.ShieldIcon);
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitHeight(30);
            playerLable.setGraphic(imageView);
        }
        /*else
        {
            image = Cards.getSpecialImage(SpecialTexture.NullIcon);
        }
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(30);
        playerLable.setGraphic(imageView);*/

        if (player.isActive_())
            playerLable.setStyle("-fx-background-color: #a0f0a0");

        GameBoard.getChildren().add(playerLable);
        addedNodesArrayList.add(playerLable);
    }


    /**
     * Draw current trump color to game board.
     *
     * @param trump     the trump color.
     */
    public void drawTrump(CardColor trump)
    {
        if (trump == null)
            return;
        ImageView imageView = new ImageView();
        Image image = Cards.getColorSymbolImage(trump);
        imageView.setImage(image);
        imageView.setCache(true);
        imageView.setLayoutX(10);
        imageView.setLayoutY(20);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(40);
        GameBoard.getChildren().add(imageView);
        addedNodesArrayList.add(imageView);
    }

    /**
     * Draws a card on the game board, if its a handcard adds a click event that tells the server that the card was clicked
     * and it adds hover entered event to show the complete card you are hovering above and it adds a hover exited event to go back to the normal state
     *
     * @param cardNumber    the card number/Id
     * @param x             the x-positon
     * @param y             the y-positon
     * @param isHandCard    hand card or played card
     */
    public void drawCard(int cardNumber, int x, int y, boolean isHandCard)
    {
        ImageView imageView = new ImageView();
        Image image = Cards.getImage(cardNumber);
        imageView.setImage(image);
        imageView.setCache(true);
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(55);
        if (isHandCard)
        {
            imageView.setOnMouseClicked(event -> cardClicked(cardNumber)); //Listener for card click
            //imageView.setOnMouseEntered(this::cardOnHover);         //Listener for onHoverEntered
            //imageView.setOnMouseExited(event -> draw(originalMessage));     //Listener for onHoverExited //For fast travel across all cards: exit is always before entrance, confirmed in testing
            addedHandCards.add(imageView);
        }
        GameBoard.getChildren().add(imageView);
        addedNodesArrayList.add(imageView);
    }

    /*
    /**
     * This method will show the complete card that the mouse is hovering above
     *
     * @param event     The complete information about the event, here only used to get its source node
     */
    /*public void cardOnHover (MouseEvent event)
    {
        boolean found = false;
        Node foundNode = null;
        int helpCounter = 0;
        ArrayList<Node> afterFound = new ArrayList<>();
        for (Node n : addedHandCards)
        {
            if (found)      afterFound.add(n);
            else            helpCounter++;
            if (event.getSource().equals(n))
            {
                found = true;
                foundNode = n;
            }
        }

        if (foundNode == null)      return;


        for (Node n : afterFound)
        {
            GameBoard.getChildren().remove(n);
            addedNodesArrayList.remove(n);
        }

        int i = 0;
        for (Node n : afterFound)
        {
            drawCard(originalMessage.getHandCards_().get(helpCounter), (int)foundNode.getLayoutX() + 55 + (i * (480 - (int)foundNode.getLayoutX() - 55) / originalMessage.getHandCards_().size()), 315, true);
            i++;
            helpCounter++;
        }
        afterFound.clear();
    }*/

    /**
     * The network handler which runs the client network connection to the server.
     *
     * @version 18.07.2021
     * @author Yann Bernhard &lt;yann.bernhard@stud.uni-hannover.de&gt;
     * @author Sebastian Kiel &lt;sebastian.kiel@stud.uni-hannover.de&gt;
     * @author Patrick Schewe &lt;p.schewe@stud.uni-hannover.de&gt;
     * @author Robert Witteck &lt;robert.witteck@stud.uni-hannover.de&gt;
     */
    class networkHandler implements Runnable
    {
        /**
         * The client socket.
         */
        Socket socket_;

        /**
         * The Input buffer.
         */
        BufferedReader inputBuffer_;

        /**
         * The Output buffer.
         */
        BufferedWriter outputBuffer_;

        /**
         * Instantiates a new client network handler.
         */
        networkHandler()
        {
            socket_ = ClientNetwork.getClientSocket_();
            inputBuffer_ = ClientNetwork.getBufferIn_();
            outputBuffer_ = ClientNetwork.getBufferOut_();
        }

        /**
         * Runs the networkHandler thread and listens for server messages.
         */
        @Override
        public void run()
        {
            System.out.print("In NetworkHandler Run\n");
            while (!killClientNetworkHandler)
            {
                System.out.print("Waiting for input from server\n");
                String line = ClientNetwork.getMessage();
                System.out.printf("Got Message %s\n", line);

                if (line != null)
                {
                    Platform.runLater(() -> executeLine(line));
                    if (line.equals("disconnect"))
                        break;
                    if (line.equals("error"))
                        break;
                    if (line.equals("GameEnded"))
                        break;
                }
            }
            ClientNetwork.stopConnection();
            System.out.println("Connection now stopped");
        }
    }
}

