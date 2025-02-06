package de.sebaty.durak.gameLogic.Cards;

import javafx.scene.image.Image;


/**
 * Control the appearance of the cards
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
     * An array containing the paths to the texture resources
     */
    private static final String[] textures =
            {
                    // 00
                    "/textures/cards/clubs/club_symbol.png",
                    "/textures/cards/clubs/club_blank_card.png",

                    // 02
                    "/textures/cards/diamonds/diamond_symbol.png",
                    "/textures/cards/diamonds/diamond_blank_card.png",

                    // 04
                    "/textures/cards/hearts/heart_symbol.png",
                    "/textures/cards/hearts/heart_blank_card.png",

                    // 06
                    "/textures/cards/spades/spade_symbol.png",
                    "/textures/cards/spades/spade_blank_card.png",

                    // 08
                    "/textures/cards/card_back_highsat.png",
                    "/textures/cards/card_back_lowsat.png",
                    "/textures/cards/card_blanc.png",

                    // 11
                    "/textures/cards/clubs/two_of_clubs.png",
                    "/textures/cards/clubs/three_of_clubs.png",
                    "/textures/cards/clubs/four_of_clubs.png",
                    "/textures/cards/clubs/five_of_clubs.png",
                    "/textures/cards/clubs/six_of_clubs.png",
                    "/textures/cards/clubs/seven_of_clubs.png",
                    "/textures/cards/clubs/eight_of_clubs.png",
                    "/textures/cards/clubs/nine_of_clubs.png",
                    "/textures/cards/clubs/ten_of_clubs.png",
                    "/textures/cards/clubs/jack_of_clubs.png",
                    "/textures/cards/clubs/queen_of_clubs.png",
                    "/textures/cards/clubs/king_of_clubs.png",
                    "/textures/cards/clubs/ace_of_clubs.png",

                    // 24
                    "/textures/cards/diamonds/two_of_diamonds.png",
                    "/textures/cards/diamonds/three_of_diamonds.png",
                    "/textures/cards/diamonds/four_of_diamonds.png",
                    "/textures/cards/diamonds/five_of_diamonds.png",
                    "/textures/cards/diamonds/six_of_diamonds.png",
                    "/textures/cards/diamonds/seven_of_diamonds.png",
                    "/textures/cards/diamonds/eight_of_diamonds.png",
                    "/textures/cards/diamonds/nine_of_diamonds.png",
                    "/textures/cards/diamonds/ten_of_diamonds.png",
                    "/textures/cards/diamonds/jack_of_diamonds.png",
                    "/textures/cards/diamonds/queen_of_diamonds.png",
                    "/textures/cards/diamonds/king_of_diamonds.png",
                    "/textures/cards/diamonds/ace_of_diamonds.png",

                    // 37
                    "/textures/cards/hearts/two_of_hearts.png",
                    "/textures/cards/hearts/three_of_hearts.png",
                    "/textures/cards/hearts/four_of_hearts.png",
                    "/textures/cards/hearts/five_of_hearts.png",
                    "/textures/cards/hearts/six_of_hearts.png",
                    "/textures/cards/hearts/seven_of_hearts.png",
                    "/textures/cards/hearts/eight_of_hearts.png",
                    "/textures/cards/hearts/nine_of_hearts.png",
                    "/textures/cards/hearts/ten_of_hearts.png",
                    "/textures/cards/hearts/jack_of_hearts.png",
                    "/textures/cards/hearts/queen_of_hearts.png",
                    "/textures/cards/hearts/king_of_hearts.png",
                    "/textures/cards/hearts/ace_of_hearts.png",

                    // 50
                    "/textures/cards/spades/two_of_spades.png",
                    "/textures/cards/spades/three_of_spades.png",
                    "/textures/cards/spades/four_of_spades.png",
                    "/textures/cards/spades/five_of_spades.png",
                    "/textures/cards/spades/six_of_spades.png",
                    "/textures/cards/spades/seven_of_spades.png",
                    "/textures/cards/spades/eight_of_spades.png",
                    "/textures/cards/spades/nine_of_spades.png",
                    "/textures/cards/spades/ten_of_spades.png",
                    "/textures/cards/spades/jack_of_spades.png",
                    "/textures/cards/spades/queen_of_spades.png",
                    "/textures/cards/spades/king_of_spades.png",
                    "/textures/cards/spades/ace_of_spades.png",

                    // 63
                    "/textures/icons/sword.png",
                    "/textures/icons/shield.png",
                    "/textures/icons/emptySymbol.png",
            };

    /**
     * This member resembles all images that can be used throughout the game
     */
    private static final javafx.scene.image.Image[] imageBuffer = new javafx.scene.image.Image[textures.length];

    /**
     * Getter image buffer size.
     *
     * @return the image buffer size
     */
    public static int getImageBufferSize()
    {
        return imageBuffer.length;
    }

    /**
     * Creates a card image according to the number which identifies each card so it will be cached or will just return the card if it already created and cached
     *
     * @param nr    the id of the image
     * @return      the newly cached image
     */
    public static javafx.scene.image.Image getImage(int nr)
    {
        if (imageBuffer[nr] == null)
        {
            javafx.scene.image.Image image = new javafx.scene.image.Image(textures[nr], 200, 10000, true, true);
            imageBuffer[nr] = image;
        }
        return imageBuffer[nr];
    }

    /**
     * Getter of the card image texture.
     *
     * @param specialTexture    enumerator of the cards
     * @return                  the card image texture
     */
    public static Image getSpecialImage(SpecialTexture specialTexture)
    {
        if (specialTexture == SpecialTexture.ClubsSymbol) return imageBuffer[0];
        if (specialTexture == SpecialTexture.ClubsBlankCard) return imageBuffer[1];
        if (specialTexture == SpecialTexture.DiamondsSymbol) return imageBuffer[2];
        if (specialTexture == SpecialTexture.DiamondsBlankCard) return imageBuffer[3];
        if (specialTexture == SpecialTexture.HeartsSymbol) return imageBuffer[4];
        if (specialTexture == SpecialTexture.HeartsBlankCard) return imageBuffer[5];
        if (specialTexture == SpecialTexture.SpadesSymbol) return imageBuffer[6];
        if (specialTexture == SpecialTexture.SpadesBlankCard) return imageBuffer[7];
        if (specialTexture == SpecialTexture.BackHighsat) return imageBuffer[8];
        if (specialTexture == SpecialTexture.BackLowsat) return imageBuffer[9];
        if (specialTexture == SpecialTexture.BlancCard) return imageBuffer[10];
        if (specialTexture == SpecialTexture.SwordIcon) return imageBuffer[63];
        if (specialTexture == SpecialTexture.ShieldIcon) return imageBuffer[64];
        if (specialTexture == SpecialTexture.NullIcon) return imageBuffer[65];
        return null;
    }

    /**
     * Getter card image color
     *
     * @param color     the color of the card
     * @return          the color symbol of the card image
     */
    public static Image getColorSymbolImage(CardColor color)
    {
        if (color == CardColor.Clubs) return getSpecialImage(SpecialTexture.ClubsSymbol);
        if (color == CardColor.Diamonds) return getSpecialImage(SpecialTexture.DiamondsSymbol);
        if (color == CardColor.Hearts) return getSpecialImage(SpecialTexture.HeartsSymbol);
        if (color == CardColor.Spades) return getSpecialImage(SpecialTexture.SpadesSymbol);
        return null;
    }


    /**
     * Used to convert the servers Color ID into a usable SpecialTexture Enum
     *
     * @param id        the id of the Color used by the server to distinguish card colors
     * @return          The enum corresponding to a SpecialTexture
     */
    public static CardColor intToCardColor(int id)
    {
        if (id == 0) return CardColor.Clubs;
        if (id == 1) return CardColor.Diamonds;
        if (id == 2) return CardColor.Hearts;
        if (id == 3) return CardColor.Spades;
        return null;
    }

}
