package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

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
            User kasutaja = new User("GUEST", socketWriter);
//             socketWriter.write("Sisestage kasutajanimi:");
//            socketWriter.newLine();
//            socketWriter.flush();
//            String nimi = socketReader.readLine();

            //socketWriter.write("Tere: " + kasutaja.toString());
            //socketWriter.newLine();
            //socketWriter.flush();
            String teade = null;
            String[] content = null;
            String message;

            while ((teade = socketReader.readLine()) != null) {
                System.out.println(teade);
                content = getPrefix(teade);

                if (content[0].equals("NICK")) {
                    kasutaja = new User(content[1], socketWriter);
                    clients.add(kasutaja);
                    //String message = ":"+kasutaja+" MODE "+kasutaja+" :+i";
                    //System.out.println(message);
                    //kasutaja.sendMessage(message);
                    message = ":127.0.0.1 001 " + kasutaja + " :Welcome to the fancy java IRC server chat";
                    System.out.println(message);
                    kasutaja.sendMessage(message);
                } else if (content[0].equals("PING")) {
                    String s = "PONG :" + content[1];
                    kasutaja.sendMessage(s);
                } else if (content[0].equals("JOIN")) {

                    if (ChatRooms.ifChatRoomExists(content[1])) {
                        System.out.println("VANA RUUM");
                        ChatRoom room = ChatRooms.getChatRoom(content[1]);
                        String roomOwner = room.getOwner().toString();
                        room.join(kasutaja);
                        message = ":" + kasutaja + "!~" + kasutaja + "@127.0.0.1 JOIN " + content[1];
                        System.out.println(message);
                        kasutaja.sendMessage(message);
                        message = ":127.0.0.1 353 " + kasutaja + " @ " + content[1] + " :" + room.getUsers() + "@" + roomOwner;
                        System.out.println(message);
                        kasutaja.sendMessage(message);
                        message = ":127.0.0.1 366 " + kasutaja + " " + content[1] + " :End of /NAMES list.";
                        System.out.println(message);
                        kasutaja.sendMessage(message);
                        message = ":" + kasutaja + "!~" + kasutaja + "@127.0.0.1 JOIN " + content[1];
                        room.sendMessage(message, kasutaja);
                    } else {
                        System.out.println("UUS RUUM");
                        ChatRoom cR = ChatRooms.newChatRoom(content[1], kasutaja);
                        cR.join(kasutaja);

                        message = ":" + kasutaja + "!" + kasutaja + "@127.0.0.1 JOIN " + content[1];
                        System.out.println(message);
                        kasutaja.sendMessage(message);

                        message = ":127.0.0.1 MODE " + content[1] + " +ns";
                        System.out.println(message);
                        kasutaja.sendMessage(message);

                        message = ":127.0.0.1 353 " + kasutaja + " @ " + content[1] + " :@" + kasutaja;
                        System.out.println(message);
                        kasutaja.sendMessage(message);

                        message = ":127.0.0.1 366 " + kasutaja + " " + content[1] + " :End of /NAMES list.";
                        System.out.println(message);
                        kasutaja.sendMessage(message);
                    }
                } else if (content[0].startsWith("PRIVMSG")) {
                    String[] subContent = content[0].split(" ");
                    String targetChatRoom = subContent[1].trim();
                    ChatRoom room = ChatRooms.getChatRoom(targetChatRoom);
                    //message = :marva!~marva@39-97-106-194.dyn.estpak.ee PRIVMSG #laheRuum :tere mmehed
                    message = ":" + kasutaja + "!~" + kasutaja + "@127.0.0.1 PRIVMSG " + targetChatRoom + " :" + content[1];
                    System.out.println(message);
                    room.sendMessage(message, kasutaja);
                } else if (content[0].equals("QUIT")) {
                    message = ":" + kasutaja + "!~" + kasutaja + "@127.0.0.1 QUIT  :Quit:" + kasutaja;

                }

                // String message = "Message from " + kasutaja + ": " + teade;
                //sendMessage(message, kasutaja);

            }
            activeSocket.close();
        } catch (SocketException e) {
            System.out.println("Klient lahkus");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(String message, User client) {

        for (User s : clients) {
            if (s != client) {
                s.sendMessage(message);
            }
        }
    }

    public static String[] getPrefix(String input) {
        CharSequence cs = " :";
        String[] s;

        if (input.contains(cs)) {
            s = input.split(" \\:");
        } else {
            s = input.split(" ");
        }
        return s;
    }
}

