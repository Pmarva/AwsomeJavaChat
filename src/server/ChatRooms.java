package server;

import java.util.ArrayList;

/**
 * Created by marva on 3.12.15.
 */
public class ChatRooms {
    private ArrayList<ChatRoom> chatRooms = new ArrayList<ChatRoom>();

    public ChatRooms() {

    }

    public ChatRoom newChatRoom(String name, User u) {
        ChatRoom cr = new ChatRoom(name, u);
        chatRooms.add(cr);
        return cr;
    }

    public void joinChatRoom(String chatroomName, User u) {

        for (ChatRoom cr : chatRooms) {
            if (cr.toString().equals(chatroomName)) {
                cr.join(u);
                String teade = u + "connected";
            }
        }
    }

    public boolean ifExists(String name) {

        for (ChatRoom room : chatRooms) {
            if (room.toString().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public ChatRoom getChatRoom(String name) {
        for (ChatRoom room : chatRooms) {
            if (room.toString().equals(name)) {
                return room;
            }
        }
        return null;
    }

    public void sendMessageToAll(String message, User u) {

          for (ChatRoom cr: chatRooms) {
              if(cr.isJoined(u)) {
                  cr.sendMessage(message);
              }
          }
    }

    public void removeUser(User u) {
        for (ChatRoom cr: chatRooms) {
            if(cr.isJoined(u)) {
                cr.leave(u);
            }
        }
    }

    public void showChatRooms() {
        for (ChatRoom room : chatRooms) {
            System.out.println(room);
        }
    }
}
