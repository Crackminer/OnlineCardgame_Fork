package de.sebaty.durak.Network;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.*;
import java.net.Socket;
import java.util.Optional;

/**
 * Controls the network settings of the client
 *
 * @version 18.07.2021
 * @author Yann Bernhard &lt;yann.bernhard@stud.uni-hannover.de&gt;
 * @author Sebastian Kiel &lt;sebastian.kiel@stud.uni-hannover.de&gt;
 * @author Patrick Schewe &lt;p.schewe@stud.uni-hannover.de&gt;
 * @author Robert Witteck &lt;robert.witteck@stud.uni-hannover.de&gt;
 */
public class ClientNetwork
{
    /**
     * The socket of this client
     */
    private static Socket clientSocket_ = null;

    /**
     * The bufferedwriter of the socket
     */
    private static BufferedWriter bufferOut_ = null;

    /**
     * The bufferedreader of the socket
     */
    private static BufferedReader bufferIn_ = null;

    /**
     * The loggedIn status of the socket-connection
     */
    private static boolean loggedIn_ = false;

    /**
     * Getter for client socket
     *
     * @return the client socket
     */
    public static Socket getClientSocket_()
    {
        return clientSocket_;
    }

    /**
     * Getter of buffer out.
     *
     * @return the buffer out
     */
    public static BufferedWriter getBufferOut_()
    {
        return bufferOut_;
    }

    /**
     * Getter buffer in.
     *
     * @return the buffer in
     */
    public static BufferedReader getBufferIn_()
    {
        return bufferIn_;
    }

    /**
     * Check whether the client is logged in
     *
     * @return true if logged in
     */
    public static boolean isLoggedIn_()
    {
        return loggedIn_;
    }

    /**
     * Start connection to server
     *
     * @param ip        the ip of the server
     * @param port      the port of the server
     * @param password  the password of the server
     * @param user      the user user name of the client
     * @return          true if connection was successful
     */
    public static boolean startConnection(String ip, String port, String password, String user)
    {
        if (loggedIn_)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connecting To Server");
            alert.setHeaderText("Already logged into Server");
            alert.setContentText("Do you want to reconnect");
            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
            alert.setResizable(true);
            alert.onShownProperty().addListener(e -> Platform.runLater(() -> alert.setResizable(false)));
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == buttonTypeNo) return true;
        }
        loggedIn_ = false;

        try
        {
            clientSocket_ = new Socket(ip, Integer.parseInt(port));
        }
        catch (IOException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connecting To Server");
            alert.setHeaderText("No connection to server");
            alert.setContentText("Please check the IP-address and your internet connection");
            alert.setResizable(true);
            alert.onShownProperty().addListener(ebox -> Platform.runLater(() -> alert.setResizable(false)));
            alert.showAndWait();
            return false;
        }

        try
        {
            bufferOut_ = new BufferedWriter(new OutputStreamWriter(clientSocket_.getOutputStream()));
            bufferIn_ = new BufferedReader(new InputStreamReader(clientSocket_.getInputStream()));
        }
        catch (IOException e)
        {
            stopConnection();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connecting To Server");
            alert.setHeaderText("Could not establish read-write stream");
            alert.setContentText("Please check the IP-address and your internet connection");
            alert.setResizable(true);
            alert.onShownProperty().addListener(ebox -> Platform.runLater(() -> alert.setResizable(false)));
            alert.showAndWait();
            return false;
        }

        //String logInMessage = "Password: " + password + " User: " + user;
        String logInMessage = "Password;" + password + ";" + " User;" + user;


        if (!sendMessage(logInMessage)) return false;

        String answer = getMessage();
        System.out.println(answer);

        if (answer.equals("failed"))
        {
            stopConnection();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connecting To Server");
            alert.setHeaderText("Server refused connection");
            alert.setContentText("Please check credentials");
            alert.setResizable(true);
            alert.onShownProperty().addListener(e -> Platform.runLater(() -> alert.setResizable(false)));
            alert.showAndWait();
            return false;
        }
        if (answer.equals("full"))
        {
            stopConnection();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connecting To Server");
            alert.setHeaderText("Server is full");
            alert.setContentText("Try another server");
            alert.setResizable(true);
            alert.onShownProperty().addListener(e -> Platform.runLater(() -> alert.setResizable(false)));
            alert.showAndWait();
            return false;
        }
        if (!answer.equals("logged in"))
        {
            stopConnection();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connecting To Server");
            alert.setHeaderText("Server send incorrect answer");
            alert.setContentText("Please check IP-address");
            alert.setResizable(true);
            alert.onShownProperty().addListener(e -> Platform.runLater(() -> alert.setResizable(false)));
            alert.showAndWait();
            return false;
        }
        loggedIn_ = true;
        return true;
    }

    /**
     * Send message to the server.
     *
     * @param msg   the message to send
     * @return      return true if send was successful
     */
    public static boolean sendMessage(String msg)
    {
        try
        {
            bufferOut_.write(msg + "\n");
            bufferOut_.flush();
        }
        catch (IOException e)
        {
            stopConnection();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Network Error");
            alert.setHeaderText("Could not send message to server");
            alert.setContentText("Try to reconnect");
            alert.setResizable(true);
            alert.onShownProperty().addListener(ebox -> Platform.runLater(() -> alert.setResizable(false)));
            alert.showAndWait();
            return false;
        }
        return true;
    }

    /**
     * Gets messages from the server.
     *
     * @return the message of the server
     */
    public static String getMessage()
    {
        if (bufferIn_ == null) return "error";
        String answer = null;
        try
        {
            while (answer == null)
            {
                answer = bufferIn_.readLine();
                if (answer != null && answer.equals("disconnect"))
                {
                    stopConnection();
                    return answer;
                }
            }
        }
        catch (IOException e)
        {
            stopConnection();
            return "connection_stopped";
        }
        return answer;
    }

    /**
     * Stop connection to the server, close all buffers and socket
     */
    public static void stopConnection()
    {
        loggedIn_ = false;
        try
        {
            if (bufferIn_ != null)
            {
                bufferIn_.close();
                bufferIn_ = null;
            }
            if (bufferOut_ != null)
            {
                bufferOut_.close();
                bufferOut_ = null;
            }
            if (clientSocket_ != null)
            {
                clientSocket_.close();
                clientSocket_ = null;
            }
        }
        catch (IOException e)
        {
            System.out.println("Exception while closing socket");
        }
    }
}
