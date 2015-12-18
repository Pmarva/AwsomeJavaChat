package server;

import java.net.BindException;

/**
 * Created by marva on 10.12.15.
 */
public class Main {
    public static void main(String[] args) {
        Server server = new Server("127.0.0.1", 6667);
        try {
            server.start();
        } catch (BindException e) {
            System.out.println("Ei leia sellist liidest");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
