package server;

/**
 * Created by marva on 10.12.15.
 */
public class Main {
    public static void main(String[] args) {
        Server server = new Server("192.168.1.76",6667);
        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
