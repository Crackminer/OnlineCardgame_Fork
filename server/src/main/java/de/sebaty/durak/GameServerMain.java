package de.sebaty.durak;

import de.sebaty.durak.Clients.ClientManager;

/**
 * The main class for the server.
 *
 * @version 18.07.2021
 *  @author Yann Bernhard &lt;yann.pablo.bernard@stud.uni-hannover.de&gt;
 *  @author Sebastian Kiel &lt;sebastian.kiel@stud.uni-hannover.de&gt;
 *  @author Patrick Schewe &lt;p.schewe@stud.uni-hannover.de&gt;
 *  @author Robert Witteck &lt;robert.witteck@stud.uni-hannover.de&gt;
 */
public class GameServerMain
{
    /**
     * The Main method of the Server
     *
     * @param args  The launch args
     */
    public static void main(String[] args)
    {
        int maxPlayerCount = 2;
        String serverPassword = "";
        int serverPort = 8000;

        String selector = "n";

        while (!selector.equals("q"))
        {
            if(selector.equals("n"))
            {
                maxPlayerCount = ServerConsoleInput.getMaxPlayers();
                serverPassword = ServerConsoleInput.getServerPassword();
                serverPort = ServerConsoleInput.getServerPort();
            }

            ServerNetwork serverNetwork = new ServerNetwork(maxPlayerCount, serverPassword, serverPort);
            serverNetwork.run();

            ClientManager.reset();
            selector = ServerConsoleInput.restartSelector();
        }
        System.exit(0);
    }
}
