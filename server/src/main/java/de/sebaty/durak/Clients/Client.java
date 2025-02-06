package de.sebaty.durak.Clients;

import java.io.BufferedWriter;

/**
 * Class to handle each connected client
 *
 * @version 18.07.2021
 * @author Yann Bernhard &lt;yann.bernhard@stud.uni-hannover.de&gt;
 * @author Sebastian Kiel &lt;sebastian.kiel@stud.uni-hannover.de&gt;
 * @author Patrick Schewe &lt;p.schewe@stud.uni-hannover.de&gt;
 * @author Robert Witteck &lt;robert.witteck@stud.uni-hannover.de&gt;
 */
public class Client
{
    private final int ID_;
    private final BufferedWriter writer_;

    /**
     * Instantiates a new Client.
     *
     * @param id     the id of the client
     * @param writer buffer writer to communicate with the client.
     */
    public Client(int id, BufferedWriter writer)
    {
        ID_ = id;
        writer_ = writer;
    }

    /**
     * Gets id of the client
     *
     * @return the id
     */
    public int getID_()
    {
        return ID_;
    }

    /**
     * Gets writer for client communication.
     *
     * @return the writer
     */
    public BufferedWriter getWriter_()
    {
        return writer_;
    }
}
