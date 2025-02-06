package de.sebaty.durak.gameLogic;

import static de.sebaty.durak.gameLogic.Cards.Cards.getCard;

/**
 * Attack action of the player.
 *
 * @version 18.07.2021
 * @author Yann Bernhard &lt;yann.bernhard@stud.uni-hannover.de&gt;
 * @author Sebastian Kiel &lt;sebastian.kiel@stud.uni-hannover.de&gt;
 * @author Patrick Schewe &lt;p.schewe@stud.uni-hannover.de&gt;
 * @author Robert Witteck &lt;robert.witteck@stud.uni-hannover.de&gt;
 */
public class Attack
{
    /**
     * Tries to play a given Card for the offense
     *
     * @param card  the id of card
     * @return      true if card was played
     */
    public static boolean attack(int card)
    {
        // No card is visible, first card will be played
        if (GameManager.visibleCards.size() == 0)
        {
            GameManager.visibleCards.add(new int[]{card, -1});
            return true;
        }

        // at least 2 cards are visible, new attack card will be played
        for (int[] intArr : GameManager.visibleCards)
        {
            for (int visibleCard : intArr)
            {
                if (getCard(visibleCard) == getCard(card))
                {
                    GameManager.visibleCards.add(new int[]{card, -1});
                    return true;
                }
            }
        }
        return false;
    }
}
