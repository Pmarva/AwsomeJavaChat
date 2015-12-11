package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * Created by Marva on 09.11.2015.
 * class to initalize basic IRC server and start it.
 * @author Marva
 * @since java 1.8
 *
 */
public class Server {
    private ArrayList<User> clients = new ArrayList<User>();
    private ChatRooms chatRooms = new ChatRooms();
    private int port = 6667;
    private String serverInetAddress;

    /**
     * Constructor to initalize server.
     * @param inetAddress - address where to start server, default port 6667.
     */
    public Server(String inetAddress) {
        this.serverInetAddress = inetAddress;
    }

    /**
     *
     * @param inetAddress - address where to start server.
     * @param p - port number where server is listening.
     */
    public Server(String inetAddress, int p) {
        this.port = p;
        this.serverInetAddress = inetAddress;
    }

    /**
     * Start server, new connection is thread.
     */
    public void start() throws IOException {

        ServerSocket serverSocket = new ServerSocket(port, 100, InetAddress.getByName(serverInetAddress));
        System.out.println("Server started at:" + serverSocket);

        while (true) {
            System.out.println("Waiting for connection ...");
            Socket activeSocket = serverSocket.accept();
            Thread t = new Thread(new ClientHandler(activeSocket, clients, chatRooms));
            t.setDaemon(true);
            t.start();
        }
    }
}

