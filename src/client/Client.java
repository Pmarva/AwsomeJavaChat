package client;

import client.views.ServerSettings;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
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

    static TextArea messages;
    Scene scene;
    Stage window;
    static Connection conn;
    private static String lineSeperator = System.lineSeparator();

    public static void main(String[] args) {
        launch(args); //koige algus, mis k2ivitab start meetodi.
    }
    String settings[];
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        BorderPane bp = new BorderPane();
        Button sendButtpon = new Button("Send message");
        TextField sisestus = new TextField();
        messages = new TextArea();
        MenuItem connect = new MenuItem("Connect");
        MenuItem serverSettings = new MenuItem("server settings");
        MenuItem exit = new MenuItem("Exit");
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        HBox bottom = new HBox();
        HBox top = new HBox();
        scene = new Scene(bp,600,600);

        sendButtpon.setDisable(true);
        messages.setDisable(true);
        sisestus.setDisable(true);
        connect.setDisable(true);

        serverSettings.setOnAction(event -> {
            settings= ServerSettings.display();
            connect.setDisable(false);
            for (String setting:settings){
                System.out.println(setting);
            }
        });

        connect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sendButtpon.setDisable(false);
                sisestus.setDisable(false);
                messages.setDisable(false);

                conn = new Connection(settings[0],settings[1]);
                conn.join();
           //  Runnable runnable = () -> conn.join();
               // Thread t = new Thread(runnable);
               // t.setDaemon(true);
             //  t.start();
            }
        });

        sendButtpon.setOnAction(event -> {
            String tekst = sisestus.getText();

            conn.sendMessage(tekst);
            sisestus.setText("");
        });

        menuFile.getItems().addAll(connect,serverSettings,exit);
        menuBar.getMenus().add(menuFile);
        menuBar.setPrefWidth(600);

        sisestus.setPrefWidth(500.0);

        top.getChildren().add(menuBar);
        bottom.getChildren().addAll(sisestus,sendButtpon);

        bp.setTop(top);
        bp.setCenter(messages);
        bp.setBottom(bottom);

        window.setScene(scene);
        window.setTitle("AwsomeJavaChat 0.01");
        window.show();
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


