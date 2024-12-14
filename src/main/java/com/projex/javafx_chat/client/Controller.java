package com.projex.javafx_chat.client;

import com.projex.javafx_chat.shared.DatabaseUtil;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button button_send;
    @FXML
    private Button button_login;
    @FXML
    private TextField tf_message;
    @FXML
    private TextField tf_username;
    @FXML
    private PasswordField pf_password;
    @FXML
    private VBox vbox_message;
    @FXML
    private ScrollPane sp_main;

    private Client client;
    private boolean isLoggedIn = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        vbox_message.heightProperty().addListener((observableValue, oldValue, newValue) -> sp_main.setVvalue((Double) newValue));

        button_send.setOnAction(event -> sendMessage());

        // Initially disable the send button until the user logs in
        button_send.setDisable(true);
    }

    @FXML
    public void handleLogin() {
        String username = tf_username.getText();
        String password = pf_password.getText();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Please enter both username and password.");
            return;
        }

        if (DatabaseUtil.authenticateUser(username, password)) {
            System.out.println("Login Successful!");
            isLoggedIn = true;

            try {
                client = new Client(new Socket("localhost", 1234), username);
                client.receiveMessageFromServer(vbox_message);
                button_send.setDisable(false);

            } catch (IOException e) {
                System.out.println("Error connecting to the server.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid User Credentials");
        }
    }

    private void sendMessage() {
        if (!isLoggedIn) {
            System.out.println("Please log in first.");
            return;
        }

        String messageToSend = tf_message.getText();
        if (!messageToSend.isEmpty()) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 5, 5, 10));

            Text text = new Text(messageToSend);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-color: rgb(239,242,255); " +
                    "-fx-background-color: rgb(15,215,242); " +
                    "-fx-background-radius: 20px;");
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(0.934, 0.945, 0.996));

            hBox.getChildren().add(textFlow);
            vbox_message.getChildren().add(hBox);

            client.sendMessageToServer(tf_username.getText() + ": " + messageToSend);
            tf_message.clear();
        }
    }

    public static void addLabel(String messageFromServer, VBox vBox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(messageFromServer);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: rgb(233,233,235); " +
                "-fx-background-radius: 20px;");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(() -> vBox.getChildren().add(hBox));
    }
}
