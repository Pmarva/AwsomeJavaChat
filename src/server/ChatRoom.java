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

    public void join(User s) {
        users.add(s);
    }

    public User getOwner() {
        return owner;
    }

    public String getUsers() {
        StringBuilder s = new StringBuilder();
        for (User u:users) {
            s.append(u+" ");
        }
        return s.toString();
    }

    public void sendMessage(String message, User u) {
        for (User s : users) {
            if (s != u) {
                s.sendMessage(message);
            }
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
