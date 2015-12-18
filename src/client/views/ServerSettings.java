package client.views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by marva on 17.12.15.
 */
public class ServerSettings {
   static  String inetAadress;
    static String port;
    public static String[] display() {

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);
        window.setTitle("Server Settings");
        window.setWidth(200.0);

        Label labelInetAdress = new Label("IP aadress:");
        Label labelPort = new Label("Port:");
        TextField tfInetAddress = new TextField();
        TextField tfPort = new TextField();
        Button btKinnita = new Button("Kinnita");
        Button btTyhista = new Button("Tühista");

        btKinnita.setOnAction(event -> {
            inetAadress = tfInetAddress.getText();
            port = tfPort.getText();
            window.close();
        });

        btTyhista.setOnAction(event -> {
            window.close();
        });

        HBox buttons = new HBox();
        buttons.getChildren().addAll(btKinnita,btTyhista);
        VBox root = new VBox();
        root.getChildren().addAll(labelInetAdress,tfInetAddress,labelPort,tfPort,buttons);
        Scene scene = new Scene(root);
        window.setScene(scene);
        window.showAndWait();
        String data[] = {inetAadress,port};
        return data;
    }
}
