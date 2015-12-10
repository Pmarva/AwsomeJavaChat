package server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Marva on 16.11.2015.
 */
public class User {
    private String name=null;
    private BufferedWriter socketWriter;

    public User(String name,BufferedWriter socketWriter) {
        this.name=name;
        this.socketWriter=socketWriter;
    }

    public void sendMessage(String message) {
        try {
            socketWriter.write(message);
            socketWriter.newLine();
            socketWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if(!(o instanceof User)) return false;
        User u = (User)o;

        if(this.name.equals(u.toString())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return name;
    }
}
