package de.sebaty.durak;

import java.util.Scanner;

/**
 * Controls the server console input.
 *
 * @version 18.07.2021
 * @author Yann Bernhard &lt;yann.pablo.bernard@stud.uni-hannover.de&gt;
 * @author Sebastian Kiel &lt;sebastian.kiel@stud.uni-hannover.de&gt;
 * @author Patrick Schewe &lt;p.schewe@stud.uni-hannover.de&gt;
 * @author Robert Witteck &lt;robert.witteck@stud.uni-hannover.de&gt;
 */
public class ServerConsoleInput
{

    /**
     * Will ask the Server hoster if he wants to start a new game with given settings, with new settings or if he wants to quit the server
     *
     * @return  A String representation of a valid input from the Server hoster
     */
    public static String restartSelector()
    {
        System.out.println("The Game is finished how do you want to proceed?\n");
        System.out.println("Enter:");
        System.out.println("n to start another game with new settings");
        System.out.println("r to start another game with same settings");
        System.out.println("q to quit");

        Scanner sc = new Scanner(System.in);
        String input = null;
        boolean validInput = false;
        do
        {
            String s = sc.nextLine();
            if (s.equals("n") || s.equals("r") || s.equals("q"))
            {
                input = s;
                validInput = true;
            }
            else
            {
                System.out.println("Your input of " + s + " is invalid, enter either n, r or q to proceed!");
            }
        }
        while (!validInput);
        return input;
    }

    /**
     * Gets maximum number of players for the server. Limited for 2 to 8 players.
     *
     * @return the maximum number of players
     */
    public static int getMaxPlayers()
    {
        System.out.println("Enter Maxplayer Number <2-8 | Blank for 2>:");
        int maxplayers = -1;
        Scanner sc = new Scanner(System.in);
        do
        {
            String s = sc.nextLine();
            if (isInt(s))
            {
                maxplayers = Integer.parseInt(s);
            }
            else
            {
                if (s.equals(""))
                {
                    maxplayers = 2;
                }
            }
            if (maxplayers < 2 || maxplayers > 8)
                System.out.println("Your input of " + maxplayers + " Maximum players is invalid, it has to be a number between 2 and 8!");
        }
        while (maxplayers < 2 || maxplayers > 8);
        return maxplayers;
    }

    /**
     * Get server password.
     *
     * @return the server password
     */
    public static String getServerPassword()
    {
        System.out.println("Enter Password <blank for none>:");
        Scanner sc = new Scanner(System.in);
        while (!sc.hasNextLine());
        return sc.nextLine();
    }

    /**
     * Gets server port. Standard port is 8000.
     *
     * @return the server port
     */
    public static int getServerPort()
    {
        System.out.println("Enter server Port <1-65535> (Leave blank for Standard Port 8000):");
        Scanner sc = new Scanner(System.in);
        int port = -1;
        do
        {
            String s = sc.nextLine();

            if (isInt(s))
            {
                port = Integer.parseInt(s);
            }
            else
            {
                if (s.equals(""))
                {
                    port = 8000;
                }
            }
            if (port < 1 || port > 65535)
                System.out.println("Your input of " + port + " as server port is invalid, it has to be a number between 1 and 65535!");
        }
        while (port < 1 || port > 65535);
        return port;
    }

    /**
     * Checks if string value is an integer.
     *
     * @param toCheck   String to check
     * @return          true if it is integer.
     */
    public static boolean isInt(String toCheck)
    {
        if (toCheck == null || toCheck.length() == 0)
            return false;
        char[] chars = toCheck.toCharArray();
        for (char character : chars)
        {
            if (character < '0' || character > '9')
            {
                return false;
            }
        }
        return true;
    }
}
