package de.sebaty.durak;

import de.sebaty.durak.gameLogic.GameManager;
import de.sebaty.durak.Clients.ClientManager;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Objects;

/**
 * Creates a server for the card game. Gets the number of players, server password and connection settings from the user
 * via system input. When the server is successfully created it instantiates and communicates with the game manager class.
 *
 * @version 18.07.2021
 * @author Yann Bernhard &lt;yann.pablo.bernard@stud.uni-hannover.de&gt;
 * @author Sebastian Kiel &lt;sebastian.kiel@stud.uni-hannover.de&gt;
 * @author Patrick Schewe &lt;p.schewe@stud.uni-hannover.de&gt;
 * @author Robert Witteck &lt;robert.witteck@stud.uni-hannover.de&gt;
 */
public class ServerNetwork
{
    /**
     * The Server socket.
     */
    ServerSocket serverSocket_;

    /**
     * The Server password.
     */
    String serverPassword_;

    /**
     * The constant maxPlayerCount.
     */
    public int maxPlayerCount_;

    /**
     * The Names.
     */
    public String[] names_;

    /**
     * Constructor for the servernetwork
     *
     * @param maxPlayerCount    The number of players that are needed, can't be greater than 8 or lesser than 2
     * @param serverPassword    The Password of the Server, can be empty
     * @param serverPort        The Port the Server is listening and running on
     */
    public ServerNetwork(int maxPlayerCount, String serverPassword, int serverPort)
    {
        maxPlayerCount_ = maxPlayerCount; //ServerConsoleInput.getMaxPlayers();
        serverPassword_ = serverPassword; //ServerConsoleInput.getServerPassword();
        try
        {
            serverSocket_ = new ServerSocket(serverPort);
            System.out.print("Server started at " + new Date() + '\n');
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        names_ = new String[maxPlayerCount];
    }

    /**
     * Runs the server.
     * Will get all clients, will start a new game after all clients are online
     */
    void run()
    {
        waitingForClients(maxPlayerCount_);
        System.out.println("All clients found");
        int[] IDs = new int[maxPlayerCount_];
        for (int i = 0; i < maxPlayerCount_; i++)
        {
            IDs[i] = ClientManager.getClientList().get(i).getID_();
        }

        System.out.println("We Are FULL");

        clientRejecter clientReject = new clientRejecter();

        Thread thread = new Thread(clientReject);
        thread.start();

        GameManager.initGameManager(IDs, names_);

        clientReject.killRejecter();

        try
        {
            serverSocket_.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Sends a message to the client.
     *
     * @param clientID the client id.
     * @param msg      the message for the client.
     */
    public static void sendMessage(int clientID, String msg)
    {
        BufferedWriter bufferOut = ClientManager.getWriter(clientID);
        try
        {
            Objects.requireNonNull(bufferOut).write(msg + "\n");
            bufferOut.flush();
        }
        catch (IOException e)
        {
            System.out.printf("Error: could not send message %s to client %d", msg, clientID);
        }
    }

    /**
     * Waits until the specified number of clients is connected.
     *
     * @param maxNumber the number of set clients
     */
    void waitingForClients(int maxNumber)
    {
        while (ClientManager.getClientCount() < maxNumber)
        {
            try
            {
                System.out.println("Server waiting for clients" + String.format("%d", ClientManager.getClientCount()));
                Socket socket = serverSocket_.accept();
                InetAddress inetAddress = socket.getInetAddress();

                System.out.print("host name: " + inetAddress.getHostName() + "\n\tIP address " + inetAddress.getHostAddress() + "\n\n");

                System.out.println("Starting new client task");
                socketHandler task = new socketHandler(socket);
                new Thread(task).start();
                while (!ClientManager.hasHandledClient)
                {
                    System.out.println("Server waiting for client handled");
                    // Waiting for new thread to handle Client (login or reject)
                }
                ClientManager.hasHandledClient = false;
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Runs the server socket and instantiates buffer reader and output to communicate with the clients. Client commands
     * can control the game manger and close the server connection.
     *
     * @version 18.07.2021
     * @author Yann Bernhard &lt;yann.bernhard@stud.uni-hannover.de&gt;
     * @author Sebastian Kiel &lt;sebastian.kiel@stud.uni-hannover.de&gt;
     * @author Patrick Schewe &lt;p.schewe@stud.uni-hannover.de&gt;
     * @author Robert Witteck &lt;robert.witteck@stud.uni-hannover.de&gt;
     */
    class socketHandler implements Runnable
    {
        /**
         * Truth value if the client if logged in or not
         */
        private boolean loggedIn;
        private final Socket socket;

        /**
         * Instantiates a new Socket handler.
         *
         * @param socket the socket
         */
        socketHandler(Socket socket)
        {
            this.loggedIn = false;
            this.socket = socket;
        }

        /**
         * Handles all actions coming from a client and going to a client, will try to log him onto the server, if that fails he will get a message saying 'failed'
         */
        @Override
        public void run()
        {
            try
            {
                InetAddress inetAddress = socket.getInetAddress();

                BufferedReader inputBuffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter outputBuffer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                int id = -1;
                int index = -1;
                try
                {
                    while (!loggedIn)
                    {
                        String line = inputBuffer.readLine();
                        if (line != null)
                        {
                            if (line.contains("Password;"))
                            {
                                String[] args = line.split(";");
                                String p = args[1];
                                String u = args[3];
                                if (p.equals(serverPassword_))
                                {
                                    if (u != null && u.length() > 0)
                                    {
                                        for (int i = 0; i < names_.length; i++)
                                        {
                                            if (names_[i] == null)
                                            {
                                                names_[i] = u;
                                                index = i;
                                                break;
                                            }
                                        }
                                    }
                                    loggedIn = true;
                                    id = ClientManager.addClient(outputBuffer);
                                    sendMessage(id, "logged in");
                                }
                                else
                                {
                                    ClientManager.hasHandledClient = true;  // handled, but not logged in
                                    outputBuffer.write("failed\n");
                                    outputBuffer.flush();
                                    socket.close();
                                    return;
                                }
                            }
                        }
                    }
                }
                catch (IOException e)
                {
                    System.out.print("Error: could not login client");
                    try
                    {
                        socket.close();
                    }
                    catch (IOException ex)
                    {
                        System.out.println("Could not close socket, but it's no problem.");
                    }
                    return;
                }

                while (true)
                {
                    //System.out.printf("Waiting for message from Client: %d\n", id);
                    String line = inputBuffer.readLine();
                    if (line != null)
                    {
                        System.out.printf("Got Message %s from Client %d\n", line, id);

                        if (line.equals("disconnect"))
                        {
                            names_[index] = null;
                            if (GameManager.isRunning())
                            {
                                GameManager.addBot(id);
                            }
                            break;
                        }
                        System.out.printf("Client wants to access gameLogic with <%s>\n", line);
                        if (line.equals("take") || line.equals("pass") || line.matches("^(1[1-9]|[2-5]\\d?|6[0-2])$"))
                        {
                            System.out.println(line);
                            GameManager.takeAction(line, id);
                            System.out.printf("Client <%d> is accessing gameLogic with <%s>\n", id, line);
                        }
                    }
                }
                socket.close();
                ClientManager.removeClient(id);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                System.err.println("Something went wrong");
            }
        }
    }

    /**
     * This class will automatically reject ever client that tries to connect to the server after all players are gotten
     *
     * @version 18.07.2021
     * @author Yann Bernhard &lt;yann.bernhard@stud.uni-hannover.de&gt;
     * @author Sebastian Kiel &lt;sebastian.kiel@stud.uni-hannover.de&gt;
     * @author Patrick Schewe &lt;p.schewe@stud.uni-hannover.de&gt;
     * @author Robert Witteck &lt;robert.witteck@stud.uni-hannover.de&gt;
     */
    class clientRejecter implements Runnable
    {
        private boolean runRejecter = true;
        /**
         * Rejects all clients that are now trying to connect to the server
         */
        @Override
        public void run()
        {
            try
            {
                serverSocket_.setSoTimeout(1000);
            }
            catch (SocketException e)
            {
                System.out.println("time out");
            }
            while (runRejecter)
            {
                try
                {
                    if (serverSocket_.isClosed())  break;
//                    System.out.println("Rejecting clients");
                    Socket socket = serverSocket_.accept();
                    InetAddress inetAddress = socket.getInetAddress();

                    System.out.print("reject client: " + inetAddress.getHostName() + "\n\tIP address " + inetAddress.getHostAddress() + "\n\n");



                    BufferedWriter tempWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    tempWriter.write("full\n");
                    tempWriter.flush();
                    socket.close();
                }
                catch ( SocketTimeoutException et )
                {
                    // ignore timeout
                }
                catch (IOException ex)
                {
//                    ex.printStackTrace();
                    // ignore empty socket when breaking socket.accept()
                }
            }
        }
        public void killRejecter(){
            runRejecter = false;
        }

    }
}
