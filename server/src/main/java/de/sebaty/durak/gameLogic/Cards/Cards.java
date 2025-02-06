package de.sebaty.durak.gameLogic.Cards;

/**
 * The type Cards.
 *
 * @version 18.07.2021
 * @author Yann Bernhard &lt;yann.bernhard@stud.uni-hannover.de&gt;
 * @author Sebastian Kiel &lt;sebastian.kiel@stud.uni-hannover.de&gt;
 * @author Patrick Schewe &lt;p.schewe@stud.uni-hannover.de&gt;
 * @author Robert Witteck &lt;robert.witteck@stud.uni-hannover.de&gt;
 */
public class Cards
{
    /**
     * Gets color.
     *
     * @param Nr        The Nr (index in the textures Array) of a Card
     * @return          One of the four Card-Colors or Undefined if the function is not called correctly
     */
    public static CardColor getColor(int Nr)
    {
        // first real card is two_of_clubs (index 11),
        // so club is index 11 till index 23 and always gives 0
        // ((11 - 11) / 13) == 0 and ((23 - 11) / 13) == 0
        // next color starts at index 24 and gives 1 und so on
        // club == 0
        // diamonds == 1
        // hearts == 2
        // spades == 3

        int cardColor = (Nr - 11) / 13;

        if (cardColor == 0)
            return CardColor.CLUB;
        if (cardColor == 1)
            return CardColor.DIAMONDS;
        if (cardColor == 2)
            return CardColor.HEARTS;
        if (cardColor == 3)
            return CardColor.SPADES;

        return (CardColor.UNDEFINED_COLOR);
    }

    /**
     * Gets color int.
     *
     * @param color     the color
     * @return          the color int
     */
    public static int getColorInt(CardColor color)
    {
        if (color == CardColor.CLUB)
            return 0;
        if (color == CardColor.DIAMONDS)
            return 1;
        if (color == CardColor.HEARTS)
            return 2;
        if (color == CardColor.SPADES)
            return 3;

        return -1;
    }

    /**
     * Gets card.
     *
     * @param Nr    The id of a Card
     * @return      The card Number in increasing order ignoring the color, so ace of clubs and ace of hearts will both return 12 If the method is not called correctly (card number lower than 11 or higher than 63) -1 will be returned
     */
    public static int getCard(int Nr)
    {
        if (Nr < 11 || Nr > 63)
            return (-1);

        return ((Nr - 11) % 13);
    }

    /**
     * Compare cards int.
     *
     * @param Trump             The Trump color
     * @param attackerCard      The Nr (index in the textures Array) of the first Card
     * @param defenderCard      The Nr (index in the textures Array) of the second Card
     * @return                  1 if Card1 will beat Nr2, -1 if Card2 will beat Card 1, 0 if both cards are equal (this should not happen)
     */
    public static int compareCards(CardColor Trump, int attackerCard, int defenderCard)
    {
        CardColor cardColorNr1 = getColor(attackerCard);
        CardColor cardColorNr2 = getColor(defenderCard);

        if (cardColorNr1 == Trump && cardColorNr2 != Trump)
            return 1;
        if (cardColorNr1 != Trump && cardColorNr2 == Trump)
            return -1;
        // card Nr1 is attack, if both of the cards color differs from one another attacker "beats" defender. this can only be done if none of the cards is a trump
        if (cardColorNr1 != cardColorNr2)
            return 1;
        return Integer.compare(attackerCard, defenderCard);
    }
}
