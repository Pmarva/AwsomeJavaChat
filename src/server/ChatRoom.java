package server;

import java.util.ArrayList;

/**
 * Created by Marva on 16.11.2015.
 * Chatroom class
 */
public class ChatRoom {

    private String name;
    private User owner=null;
    private ArrayList<User> users = new ArrayList<User>();

    /**
     * Constructor
     * @param name - String name for Chatroom
     * @param owner - User who creates new room.
     */
    public ChatRoom(String name, User owner) {
        this.name=name;
        this.owner=owner;
    }

    /**
     * add user to channel.
     * @param u - user who wants to join.
     *
     */
    public void join(User u) {
        users.add(u);
    }

    /**
     * Leave user from chatroom.
     * @param u User who wants to leave.
     */
    public void leave(User u) {
        users.remove(u);
    }

    /**
     * Return channel owner.
     * @return User owner of chatroom
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Return joined users. As long string
     * @param s - User how is scipped.
     * @return long string with suer name "kalle malle juta"
     */
    public String getUsers(User s) {
        StringBuilder sb = new StringBuilder();
        for (User u:users) {
            if(!s.equals(u)) {
                sb.append(u+" ");
            }
        }
        return s.toString();
    }

    /**
     * SendMessage to all useres exept user how is in input.
     * @param message - String message to sent
     * @param u - user who will be scipped.
     */
    public void sendMessage(String message, User u) {
        for (User s : users) {
            if (!s.equals(u)) {
                s.sendMessage(message);
            }
        }
    }

    /**
     * Send message to all users in channel
     * @param message String message to all users.
     */
    public void sendMessage(String message) {
        for (User s : users) {
                s.sendMessage(message);
        }
    }

    /**
     * Check if user is joined with chatroom
     * @param u - user who check
     * @return
     */
    public boolean isJoined(User u) {
       return users.contains(u);
    }

    /**
     * return object to string, return user name.
     * @return
     */
    @Override
    public String toString() {
        return name;
    }
}
