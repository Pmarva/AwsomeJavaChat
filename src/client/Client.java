package client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

/**
 * Created by Marva on 09.11.2015.
 */
public class Client extends Application {
    public static BufferedReader socketReader;
    Scene scene;
    Stage window;

    public static void main(String[] args) {
        launch(args); //koige algus, mis k2ivitab start meetodi.
        try {
            String lineSeperator = System.getProperty("line.separator");
            Socket socket = new Socket("irc.freenode.net", 6667);
            System.out.println("Connected to:" + socket);
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            String outMsg = null;
            Runnable runnable = () -> getFromServer(socketWriter);
            new Thread(runnable).start();

            //Thread.sleep(1000);
            socketWriter.write("NICK Tobe");
            socketWriter.newLine();
            socketWriter.flush();
            socketWriter.write("USER  malle 8 *  : kalle Mutton");
            socketWriter.newLine();
            socketWriter.flush();


            while((outMsg = consoleReader.readLine()) != null) {
                socketWriter.write(outMsg);
                socketWriter.write(lineSeperator);
                socketWriter.flush();
            }

            socket.close();
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

    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;
        BorderPane bp = new BorderPane();
        Button sendButtpon = new Button("Send message");
        TextField sisestus = new TextField();
        sisestus.setPrefWidth(500.0);
        HBox hbox = new HBox();
        hbox.getChildren().addAll(sisestus,sendButtpon);
        bp.setBottom(hbox);

        scene = new Scene(bp,600,600);
        window.setScene(scene);
        window.setTitle("AwsomeJavaChat 0.01");
        window.show();
    }

    public static void getFromServer(BufferedWriter bfWrtr){
        String inMsg = null;

        try {
            while(true) {
                inMsg = socketReader.readLine();
                String[] s = getPrefix(inMsg);

                if(inMsg.equals("null")){
                    break;
                } else if(s[0].equals("PING")) {
                    System.out.println(s[0]+" "+s[1]);
                    bfWrtr.write("PONG "+s[1]);
                    bfWrtr.newLine();
                    bfWrtr.flush();
                }
                System.out.println(inMsg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[] getPrefix(String input) {
        CharSequence cs = " :";
        String[] s;
        if(input.contains(cs)) {
            s = input.split(" \\:");
        } else {
            s = input.split(" ");
        }
        return s;
    }
}
