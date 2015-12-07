package server;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Created by Marva on 16.11.2015.
 */
public class User {
    private String name=null;
    private BufferedWriter socketWriter;
    private String mode;

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
    public String toString() {
        return name;
    }
}
