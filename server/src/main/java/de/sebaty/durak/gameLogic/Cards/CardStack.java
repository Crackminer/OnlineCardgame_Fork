package de.sebaty.durak.gameLogic.Cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Creates a card stack between the values: int 11 - 63
 *
 * @version 18.07.2021
 * @author Yann Bernhard &lt;yann.bernhard@stud.uni-hannover.de&gt;
 * @author Sebastian Kiel &lt;sebastian.kiel@stud.uni-hannover.de&gt;
 * @author Patrick Schewe &lt;p.schewe@stud.uni-hannover.de&gt;
 * @author Robert Witteck &lt;robert.witteck@stud.uni-hannover.de&gt;
 */
public class CardStack
{
    /**
     * The stack of the cards.
     */
    private final List<Integer> stack;

    /**
     * Creates the deck.
     */
    public CardStack()
    {
        int cardStart = 11;
        int cardEnd = 62;

        this.stack = new ArrayList<>();
        for (int i = cardStart; i <= cardEnd; i++)
        {
            this.stack.add(i);
        }

        shuffleList();
    }

    /**
     * Creates a readable string of the deck.
     *
     * @return the described string.
     */
    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder();
        for (int x : this.stack)
        {
            str.append(String.format("Colour: %s\tValue: %s\n", Cards.getColor(x), Cards.getCard(x)));
        }
        return str.toString();
    }

    /**
     * Shuffles the card stack.
     */
    public void shuffleList()
    {
        Collections.shuffle(stack);
        Collections.shuffle(stack);
    }

    /**
     * Size of the deck.
     *
     * @return Size of the deck.
     */
    public int remainingCards()
    {
        return stack.size();
    }

    /**
     * Returns the first card of the deck.
     *
     * @return the first card of the deck. Else returns null if List is empty!
     */
    public Integer getFirstCard()
    {
        if (!stack.isEmpty())   return stack.get(0);
        else                    return null;
    }

    /**
     * Returns the last card of the deck.
     *
     * @return the last card of the deck. Else returns null if List is empty!
     */
    public Integer getLastCard()
    {
        if (!stack.isEmpty())   return stack.get(stack.size() - 1);
        else                    return null;
    }

    /**
     * Pops the first card of the deck.
     */
    public void popFirstCard()
    {
        if (!stack.isEmpty())   stack.remove(0);
    }
}
