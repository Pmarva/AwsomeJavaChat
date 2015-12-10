package server;

import java.util.ArrayList;

/**
 * Created by Marva on 16.11.2015.
 */
public class ChatRoom {

    private String name;
    private User owner=null;
    private ArrayList<User> users = new ArrayList<User>();

    public ChatRoom(String name, User owner) {
        this.name=name;
        this.owner=owner;
    }

    public void join(User u) {
        users.add(u);
    }

    public void leave(User u) {
        users.remove(u);
    }

    public User getOwner() {
        return owner;
    }

    public String getUsers(User s) {
        StringBuilder sb = new StringBuilder();
        for (User u:users) {
            if(!s.equals(u)) {
                sb.append(u+" ");
            }
        }
        return s.toString();
    }

    public void sendMessage(String message, User u) {
        for (User s : users) {
            if (!s.equals(u)) {
                s.sendMessage(message);
            }
        }
    }

    public void sendMessage(String message) {
        for (User s : users) {
                s.sendMessage(message);
        }
    }

    public boolean isJoined(User u) {
       return users.contains(u);
    }

    @Override
    public String toString() {
        return name;
    }
}
