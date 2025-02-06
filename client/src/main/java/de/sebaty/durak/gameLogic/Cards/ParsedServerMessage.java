package de.sebaty.durak.gameLogic.Cards;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Evaluates the message from the server.
 *
 * @version 18.07.2021
 * @author Yann Bernhard &lt;yann.bernhard@stud.uni-hannover.de&gt;
 * @author Sebastian Kiel &lt;sebastian.kiel@stud.uni-hannover.de&gt;
 * @author Patrick Schewe &lt;p.schewe@stud.uni-hannover.de&gt;
 * @author Robert Witteck &lt;robert.witteck@stud.uni-hannover.de&gt;
 */
public class ParsedServerMessage
{
    /**
     * The Draw pile height.
     */
    int drawPileHeight_;
    /**
     * The Trump color.
     */
    CardColor trumpColor_;
    /**
     * The Players.
     */
    ArrayList<Player> players_ = new ArrayList<>();
    /**
     * The Visible cards list.
     */
    ArrayList<int[]> visibleCards_ = new ArrayList<>();
    /**
     * The Hand cards list.
     */
    ArrayList<Integer> handCards_ = new ArrayList<>();
    /**
     * True if the evaluation was successfull.
     */
    Boolean wasSuccessful_;

    /**
     * Getter draw pile height.
     *
     * @return the draw pile height
     */
    public int getDrawPileHeight_()
    {
        return drawPileHeight_;
    }

    /**
     * Getter trump color.
     *
     * @return the trump color
     */
    public CardColor getTrumpColor_()
    {
        return trumpColor_;
    }

    /**
     * Getter players.
     *
     * @return the players
     */
    public ArrayList<Player> getPlayers_()
    {
        return players_;
    }

    /**
     * Getter visible cards.
     *
     * @return the visible cards
     */
    public ArrayList<int[]> getVisibleCards_()
    {
        return visibleCards_;
    }

    /**
     * Getter hand cards.
     *
     * @return the hand cards
     */
    public ArrayList<Integer> getHandCards_()
    {
        return handCards_;
    }

    /**
     * Getter if move was successful.
     *
     * @return true if was successful
     */
    public Boolean getWasSuccessful_()
    {
        return wasSuccessful_;
    }

    /**
     * Player class which controls the id, name, hancards and status (attacker, defender, active) of the player.
     *
     * @version 18.07.2021
     * @author Yann Bernhard &lt;yann.bernhard@stud.uni-hannover.de&gt;
     * @author Sebastian Kiel &lt;sebastian.kiel@stud.uni-hannover.de&gt;
     * @author Patrick Schewe &lt;p.schewe@stud.uni-hannover.de&gt;
     * @author Robert Witteck &lt;robert.witteck@stud.uni-hannover.de&gt;
     */
    public class Player
    {
        /**
         * The Id.
         */
        int id_;
        /**
         * The Name.
         */
        String name_;
        /**
         * The Hand card amount.
         */
        int handCardAmount_;
        /**
         * Attacker state.
         */
        boolean attacker_;
        /**
         * Defender state.
         */
        boolean defender_;
        /**
         * Active state.
         */
        boolean active_;

        /**
         * Instantiates a new Player.
         *
         * @param id             the id
         * @param name           the name
         * @param handCardAmount the hand card amount
         * @param attacker       attacker state
         * @param defender       defender state
         * @param active         active state
         */
        public Player(int id, String name, int handCardAmount, int attacker, int defender, int active)
        {
            id_ = id;
            name_ = name;
            handCardAmount_ = handCardAmount;
            attacker_ = (attacker == 1);
            defender_ = (defender == 1);
            active_ = (active == 1);
        }
        /**
         * Getter id.
         *
         * @return the id
         */
        public int getId_()
        {
            return id_;
        }

        /**
         * Getter name.
         *
         * @return the name
         */
        public String getName_()
        {
            return name_;
        }

        /**
         * Getter hand card amount.
         *
         * @return the hand card amount
         */
        public int getHandCardAmount_()
        {
            return handCardAmount_;
        }

        /**
         * Getter attacker state.
         *
         * @return the boolean
         */
        public boolean isAttacker_()
        {
            return attacker_;
        }

        /**
         * Getter defender state.
         *
         * @return the boolean
         */
        public boolean isDefender_()
        {
            return defender_;
        }

        /**
         * Getter active state.
         *
         * @return the boolean
         */
        public boolean isActive_()
        {
            return active_;
        }
    }

    /**
     * Instantiates a scanner to evaluate the server message with all relevant information for that player
     *
     * @param serverMsg the server message
     */
    public ParsedServerMessage(String serverMsg)
    {
        Scanner s = new Scanner(serverMsg);
        drawPileHeight_ = s.nextInt();
        trumpColor_ = Cards.intToCardColor(s.nextInt());
        int playerCount = s.nextInt();
        for (int i = 0; i < playerCount; i++)
        {
            int id = s.nextInt();
            String name = s.next();
            int handCardAmount = s.nextInt();
            int attacker = s.nextInt();
            int defender = s.nextInt();
            int active = s.nextInt();
            Player player = new Player(id, name, handCardAmount, attacker, defender, active);
            players_.add(player);
        }
        int visibleCardCount = s.nextInt();
        for (int i = 0; i < visibleCardCount; i++)
        {
            int[] arr = new int[2];
            arr[0] = s.nextInt();
            if (i + 1 < visibleCardCount)
            {
                arr[1] = s.nextInt();
                i++;
            }
            else
            {
                arr[1] = -1;
            }
            visibleCards_.add(arr);
        }
        int handCardCount = s.nextInt();
        for (int i = 0; i < handCardCount; i++)
        {
            handCards_.add(s.nextInt());
        }
        wasSuccessful_ = (s.nextInt() == 1);
    }

}
