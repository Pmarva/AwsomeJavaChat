package server;

import java.util.ArrayList;

/**
 * Created by marva on 3.12.15.
 * Holding and admins chatrooms.
 */
public class ChatRooms {
    private ArrayList<ChatRoom> chatRooms = new ArrayList<ChatRoom>();

    /**
     * Creating new chat room.
     * @param name - String for channel name
     * @param u - user who will be owner of the chatroom
     * @return - created new chatroom
     */
    public ChatRoom newChatRoom(String name, User u) {
        ChatRoom cr = new ChatRoom(name, u);
        chatRooms.add(cr);
        return cr;
    }

    /**
     * Joining a user to the chatroom.
     * @param chatroomName - ChatRoom name where to join.
     * @param u - User who want to join.
     */
    public void joinChatRoom(String chatroomName, User u) {

        for (ChatRoom cr : chatRooms) {
            if (cr.toString().equals(chatroomName)) {
                cr.join(u);
                String teade = u + "connected";
            }
        }
    }

    /**
     * Check if chatroom exists.
     * @param name - ChatRoom name where to what to check.
     */
    public boolean ifExists(String name) {

        for (ChatRoom room : chatRooms) {
            if (room.toString().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * if you want specific chatroom.
     * @param name - ChatRoom name what you want.
     * @return ChatRoom if found the return else null.
     */
    public ChatRoom getChatRoom(String name) {
        for (ChatRoom room : chatRooms) {
            if (room.toString().equals(name)) {
                return room;
            }
        }
        return null;
    }

    /**
     * Sennd message to all channel where user is joined.
     * @param message - String message what to send.
     * @param u - Corrent user.
     *
     */
    public void sendMessageToAll(String message, User u) {

          for (ChatRoom cr: chatRooms) {
              if(cr.isJoined(u)) {
                  cr.sendMessage(message);
              }
          }
    }

    /**
     * Remove client from chatrooms.
     * @param u User current user.
     */
    public void removeUser(User u) {
        for (ChatRoom cr: chatRooms) {
            if(cr.isJoined(u)) {
                cr.leave(u);
            }
        }
    }

    /**
     * Show list of chatrooms in server. Client ask for list.
     *
     */
    public void showChatRooms() {
        for (ChatRoom room : chatRooms) {
            System.out.println(room);
        }
    }
}
