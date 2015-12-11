package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * Created by Marva on 09.11.2015.
 */
public class Server {

    private ArrayList<User> clients = new ArrayList<User>();
    private ChatRooms chatRooms = new ChatRooms();
    private int port=6667;
    private String serverInetAddress;

    public Server(String inetAddress) {
        this.serverInetAddress=inetAddress;
    }

    public Server(String inetAddress,int p) {
        this.port=p;
        this.serverInetAddress=inetAddress;
    }

    public void start() throws IOException {

        ServerSocket serverSocket = new ServerSocket(port, 100,InetAddress.getByName(serverInetAddress));
        System.out.println("Server started at:" + serverSocket);

        while (true) {
            System.out.println("Waiting for connection ...");
            Socket activeSocket = serverSocket.accept();
            Thread t = new Thread(new ClientHandler(activeSocket,clients,chatRooms));
            t.setDaemon(true);
            t.start();
        }
    }
}

