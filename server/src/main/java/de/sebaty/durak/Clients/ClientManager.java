package de.sebaty.durak.Clients;

import java.io.BufferedWriter;
import java.util.ArrayList;

/**
 * The Client manager. Counts all client, creates a list of clients, adds and removes client to that list.
 *
 * @version 18.07.2021
 * @author Yann Bernhard &lt;yann.bernhard@stud.uni-hannover.de&gt;
 * @author Sebastian Kiel &lt;sebastian.kiel@stud.uni-hannover.de&gt;
 * @author Patrick Schewe &lt;p.schewe@stud.uni-hannover.de&gt;
 * @author Robert Witteck &lt;robert.witteck@stud.uni-hannover.de&gt;
 */
public class ClientManager
{
    /**
     * The ArrayList of all Clients currently connected to the server
     */
    private static ArrayList<Client> clientList = new ArrayList<>();

    /**
     * The New id.
     */
    static int newID = 0;

    /**
     * The constant hasHandledClient, created to synchronize the Threads
     */
    public static boolean hasHandledClient = false;

    /**
     * Add client to list
     *
     * @param writer the writer
     * @return the int
     */
    public static int addClient(BufferedWriter writer)
    {
        clientList.add(new Client(newID,writer));
        hasHandledClient = true;
        return newID++;
    }

    /**
     * Getter client list.
     *
     * @return the client list
     */
    public static ArrayList<Client> getClientList()
    {
        return clientList;
    }

    /**
     * Remove client from list
     *
     * @param id the id
     */
    public static void removeClient(int id)
    {
        for(Client c: clientList)
        {
            if(c.getID_() == id)
            {
                clientList.remove(c);
                break;
            }
        }
    }

    /**
     * Getter client writer
     *
     * @param id the id
     * @return the writer
     */
    public static BufferedWriter getWriter(int id)
    {
        for(Client c: clientList)
        {
            if(c.getID_() == id)
            {
                return c.getWriter_();
            }
        }
        return null;
    }

    /**
     * Gets client count.
     *
     * @return the client count
     */
    public static int getClientCount()
    {
        return clientList.size();
    }

    /**
     * Resets everything within the clientManager
     */
    public static void reset()
    {
        clientList.clear();
        newID = 0;
        hasHandledClient = false;
    }
}
