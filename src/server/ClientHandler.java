package server;

import client.Client;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * Created by marva on 11.12.15.
 */
public class ClientHandler implements Runnable {
    private Socket activeSocket;
    private BufferedWriter socketWriter;
    private ArrayList<User> clients;
    private User user;
    private String serverInetAddress,clientIntetAddress, message, input, command, response;
    private ChatRooms cr;

    public ClientHandler(Socket activeSocket, ArrayList<User> clients, ChatRooms cr) {
        this.activeSocket=activeSocket;
        this.clients=clients;
        this.cr=cr;
    }

    @Override
    public void run() {
        try {
            socketWriter = new BufferedWriter(new OutputStreamWriter(activeSocket.getOutputStream()));
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(activeSocket.getInputStream()));
            user = new User("GUEST", socketWriter);
            serverInetAddress = activeSocket.getLocalAddress().getHostAddress().toString();
            clientIntetAddress = activeSocket.getInetAddress().getHostAddress().toString();

            while ((input = socketReader.readLine()) != null) {
                System.out.println(input);
                clientMessages();
            }
            activeSocket.close();
        } catch (SocketException e) {
            System.out.println("Klient lahkus");
        } catch (IOException e) {
            System.out.println("Klient lahkus");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clientMessages() {

        command = InputParse.getCommand(input);
        response = InputParse.getResponse(input);

        switch (command) {
            case "NICK": nickCommand();  break;
            case "PING": pingCommand(); break;
            case "JOIN": joinCommand(); break;
            case "PRIVMSG": privMsgCommand(); break;
            case "PART": partCommand(); break;
            case "QUIT": quitCommand(); break;
            default: System.out.printf("Invalid command %s\n", command);
        }
    }

    private void nickCommand() {
        if(user.toString().equals("GUEST")) {
            System.out.println("UUS KASUTAJA");
            user = new User(response, socketWriter);
            clients.add(user);
            message = ":" + serverInetAddress + " 001 " + user + " :Welcome to the fancy java IRC server chat";
            user.sendMessage(message);
            message = ":" + serverInetAddress + " 376 " + user + " :End of /MOTD command.";
            user.sendMessage(message);
        }
    }

    private void pingCommand() {
        String s = "PONG :" + response;
        user.sendMessage(s);
    }

    private void joinCommand() {
        String channelName = InputParse.getChannelName(input);

        if (cr.ifExists(channelName)) {
            joinOldChatRoom(channelName);
        } else {
            joinNewChatRoom(channelName);
        }
    }

    private void joinOldChatRoom(String channelName) {
        ChatRoom room = cr.getChatRoom(channelName);
        System.out.println("VANA RUUM");
        String roomOwner = room.getOwner().toString();
        room.join(user);
        message = ":" + user + "!~" + user + "@"+clientIntetAddress+" JOIN " + channelName;
        System.out.println(message);
        user.sendMessage(message);
        message = ":" + serverInetAddress + " 353 " + user + " = " + channelName + " :" + room.getUsers(user) + " @" + roomOwner;
        System.out.println(message);
        user.sendMessage(message);
        message = ":" + serverInetAddress + " " + user + " " + channelName + " :End of /NAMES list.";
        System.out.println(message);
        user.sendMessage(message);
        message = ":" + user + "!~" + user + "@"+clientIntetAddress+" JOIN " + channelName;
        room.sendMessage(message,user);
    }

    private void joinNewChatRoom(String channelName) {
        System.out.println("UUS RUUM");
        ChatRoom room = cr.newChatRoom(channelName, user);
        room.join(user);

        message = ":" + user + "!" + user + "@"+clientIntetAddress+" JOIN " + channelName;
        System.out.println(message);
        user.sendMessage(message);

        message = ":" + serverInetAddress + " MODE " + channelName + " +ns";
        System.out.println(message);
        user.sendMessage(message);

        message = ":" + serverInetAddress + " 353 " + user + " = " + channelName + " :@" + user;
        System.out.println(message);
        user.sendMessage(message);

        message = ":" + serverInetAddress + " 366 " + user + " " + channelName + " :End of /NAMES list.";
        System.out.println(message);
        user.sendMessage(message);
    }

    private void privMsgCommand() {
        String targetChatRoom = InputParse.getChannelName(input);
        ChatRoom room = cr.getChatRoom(targetChatRoom);

        if(room.isJoined(user)) {
            message = ":" + user + "!~" + user + "@"+clientIntetAddress+" PRIVMSG " + targetChatRoom + " :" + response;
            System.out.println(message);
            room.sendMessage(message, user);
        } else {
            message = ":" + serverInetAddress + " 404 " + user + " " + targetChatRoom + " :Cannot send to channel";
            System.out.println(message);
            user.sendMessage(message);
        }
    }

    private void partCommand() {
        String channelName = InputParse.getChannelName(input);
        ChatRoom room = cr.getChatRoom(channelName);
        message = ":" + user + "!~" + user + "@" + clientIntetAddress + " PART " + channelName + " :" + response;
        System.out.println(message);
        room.sendMessage(message);
        room.leave(user);
    }

    private void quitCommand() {
        message = ":" + user + "!~" + user + "@"+clientIntetAddress+" QUIT  :Quit:" + response;
        cr.sendMessageToAll(message,user);
        message = "ERROR :Closing Link :"+clientIntetAddress+" (Quit: "+response+")";
        user.sendMessage(message);
        cr.removeUser(user);
        clients.remove(user);
    }
}
