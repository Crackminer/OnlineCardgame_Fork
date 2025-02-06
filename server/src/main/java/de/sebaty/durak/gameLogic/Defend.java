package de.sebaty.durak.gameLogic;

import de.sebaty.durak.gameLogic.Cards.Cards;

/**
 * Defend action of the player.
 *
 * @version 18.07.2021
 * @author Yann Bernhard &lt;yann.bernhard@stud.uni-hannover.de&gt;
 * @author Sebastian Kiel &lt;sebastian.kiel@stud.uni-hannover.de&gt;
 * @author Patrick Schewe &lt;p.schewe@stud.uni-hannover.de&gt;
 * @author Robert Witteck &lt;robert.witteck@stud.uni-hannover.de&gt;
 */
public class Defend
{
    /**
     * Tries to play a given card for the defense
     *
     * @param card      the id of the card
     * @return          true if card was played
     */
    public static boolean defend(int card)
    {
        int[] toDefend = GameManager.visibleCards.get(GameManager.visibleCards.size() - 1);
        if (Cards.compareCards(GameManager.getTrump(), toDefend[0], card) >= 0)
        {
            return false;
        }
        else if (Cards.compareCards(GameManager.getTrump(), toDefend[0], card) < 0)
        {
            toDefend[1] = card;
            GameManager.visibleCards.set(GameManager.visibleCards.size() - 1, toDefend);
            return true;
        }
        return false;
    }
}
