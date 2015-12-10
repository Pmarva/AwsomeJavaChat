package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Marva on 09.11.2015.
 */
public class Server {

    public static ArrayList<User> clients = new ArrayList<User>();

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(6667, 100, InetAddress.getByName("Localhost"));
        System.out.println("Server started at:" + serverSocket);

        while (true) {
            System.out.println("Waiting for connection ...");
            Socket activeSocket = serverSocket.accept();
            Runnable runnable = () -> clientCommunication(activeSocket);
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            t.start();
        }
    }

    public static void clientCommunication(Socket activeSocket) {
        try {
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(activeSocket.getInputStream()));
            BufferedWriter socketWriter = new BufferedWriter(new OutputStreamWriter(activeSocket.getOutputStream()));

            User user = new User("GUEST", socketWriter);
            String serverInetAddress = "127.0.0.1.server";
            String clientIntetAddress = activeSocket.getInetAddress().getHostAddress().toString()+".client";
            String input = null;
            String message;

            while ((input = socketReader.readLine()) != null) {
                System.out.println(input);

                String command = InputParse.getCommand(input);
                String response = InputParse.getResponse(input);

                if (command.equals("NICK")) {
                    user = new User(response, socketWriter);
                    clients.add(user);
                    message = ":" + serverInetAddress + " 001 " + user + " :Welcome to the fancy java IRC server chat";
                    user.sendMessage(message);
                    message = ":" + serverInetAddress + " 376 " + user + " :End of /MOTD command.";
                    user.sendMessage(message);
                } else if (command.equals("PING")) {
                    String s = "PONG :" + response;
                    user.sendMessage(s);
                } else if (command.equals("JOIN")) {
                    String channelName = InputParse.getChannelName(input);

                    if (ChatRooms.ifExists(channelName)) {
                        ChatRoom room = ChatRooms.getChatRoom(channelName);
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
                    } else {
                        System.out.println("UUS RUUM");
                        ChatRoom cR = ChatRooms.newChatRoom(channelName, user);
                        cR.join(user);

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
                } else if (command.startsWith("PRIVMSG")) {

                    String targetChatRoom = InputParse.getChannelName(input);
                    ChatRoom room = ChatRooms.getChatRoom(targetChatRoom);
                    message = ":" + user + "!~" + user + "@"+clientIntetAddress+" PRIVMSG " + targetChatRoom + " :" + response;
                    System.out.println(message);
                    room.sendMessage(message, user);

                } else if (command.equals("PART")) {

                    String channelName = InputParse.getChannelName(input);
                    ChatRoom room = ChatRooms.getChatRoom(channelName);
                    message = ":" + user + "!~" + user + "@" + clientIntetAddress + " PART " + channelName + " :" + response;
                    System.out.println(message);
                    room.sendMessage(message);
                    room.leave(user);

                } else if (command.equals("QUIT")) {
                    message = ":" + user + "!~" + user + "@"+clientIntetAddress+" QUIT  :Quit:" + response;
                    ChatRooms.sendMessageToAll(message,user);
                    message = "ERROR :Closing Link :"+clientIntetAddress+" (Quit: "+response+")";
                    user.sendMessage(message);
                    ChatRooms.removeUser(user);
                    activeSocket.close();
                }
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
}

