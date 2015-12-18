package client;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

/**
 * Created by marva on 18.12.15.
 */
public class Connection  {
    private int port = 6667;
    private String inetAddress;
    private BufferedReader socketReader;
    private BufferedWriter socketWriter;
    private static String lineSeperator = System.lineSeparator();

    public Connection(String inetAddress, String port) {
        this.port = Integer.parseInt(port);
        this.inetAddress = inetAddress;
    }

    public Connection(String inetAddress) {
        this.inetAddress = inetAddress;
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

    public void join() {
        try {
            String lineSeperator = System.getProperty("line.separator");
            Socket socket = new Socket(inetAddress, port);
            System.out.println("Connected to:" + socket);
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
           // BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            String outMsg = null;
           Runnable runnable = () -> getFromServer(socketWriter);
           new Thread(runnable).start();

            //Thread.sleep(1000);
            String teade = "NICK julla";
            System.out.println(teade);
            socketWriter.write(teade);
            socketWriter.newLine();
            socketWriter.flush();
            teade = "USER  juta 8 *  : kalle Mutton";
            System.out.println(teade);
            socketWriter.write(teade);
            socketWriter.newLine();
            socketWriter.flush();


           // while((outMsg = consoleReader.readLine()) != null) {
            //    socketWriter.write(outMsg);
            //    socketWriter.write(lineSeperator);
             //   socketWriter.flush();
            //}

           // socket.close();
        } catch(ConnectException e) {
            System.out.println("Server ei vasta");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch(Exception e) {
            System.out.println("Errrrror");
            System.out.println(e);
        }
    }

    public void getFromServer(BufferedWriter bfWrtr){
        String inMsg = null;

        try {
            while(true) {
                inMsg = socketReader.readLine();
                // String[] s = getPrefix(inMsg);
                String command = commandParse.getCommand(inMsg);
                String result = commandParse.getResponse(inMsg);

                if(inMsg.equals("null")){
                    break;
                } else if(command.equals("PING")) {
                    System.out.println(command+" "+result);
                    bfWrtr.write("PONG "+result);
                    bfWrtr.newLine();
                    bfWrtr.flush();
                }

                System.out.println(inMsg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
