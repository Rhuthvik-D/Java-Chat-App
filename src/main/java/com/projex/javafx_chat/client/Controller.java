//----------------------------------------------works-------------------------------------------------------------------

//package com.projex.javafx_chat.client;
//
//import javafx.application.Platform;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Text;
//import javafx.scene.text.TextFlow;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//import java.net.Socket;
//import java.net.URL;
//import java.util.ResourceBundle;
//
//public class Controller implements Initializable {
//
//    @FXML
//    private VBox vbox_message;
//
//    @FXML
//    private ScrollPane sp_main;
//
//    @FXML
//    private TextField tf_message;
//
//    @FXML
//    private TextField tf_username;
//
//    @FXML
//    private TextField tf_newUsername;
//
//    @FXML
//    private PasswordField pf_password;
//
//    @FXML
//    private PasswordField pf_newPassword;
//
//    @FXML
//    private Button button_send;
//
//    private Client client;
//
//    private boolean isAuthenticated = false;
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        if (vbox_message != null) { // Check if vbox_message is not null
//            vbox_message.heightProperty().addListener((observable, oldValue, newValue) -> sp_main.setVvalue((Double) newValue));
//        }
//        button_send.setDisable(true); // Disable send button until user logs in
//    }
//
//    @FXML
//    public void handleLogin() {
//        String username = tf_username.getText().trim();
//        String password = pf_password.getText().trim();
//
//        if (username.isEmpty() || password.isEmpty()) {
//            System.out.println("Please enter both username and password.");
//            return;
//        }
//
//        new Thread(() -> {
//            try {
//                // Create a new Client object for login
//                client = new Client(new Socket("localhost", 1234), username);
//
//                System.out.println("Sending LOGIN action...");
//                client.sendMessageToServer("LOGIN");  // Step 1: Send action
//                client.sendMessageToServer(username); // Step 2: Send username
//                client.sendMessageToServer(password); // Step 3: Send password
//
//                String response = client.receiveMessage();
//                System.out.println("Server Response: " + response);
//
//                if (response.contains("Login successful")) {
//                    isAuthenticated = true;
//
//                    Platform.runLater(() -> {
//                        enableChatInterface();
//                        System.out.println("Login successful.");
//                    });
//
//                    client.receiveMessageFromServer(vbox_message); // Start listening for incoming messages
//                } else {
//                    Platform.runLater(() -> System.out.println("Invalid credentials."));
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                Platform.runLater(() -> System.out.println("Error connecting to server."));
//            }
//        }).start();
//    }
//
//
//
//
//
//
//
//    @FXML
//    public void handleSignUp() {
//        String username = tf_newUsername.getText().trim();
//        String password = pf_newPassword.getText().trim();
//
//        if (username.isEmpty() || password.isEmpty()) {
//            System.out.println("Please fill out both fields.");
//            return;
//        }
//
//        try {
//            client = new Client(new Socket("localhost", 1234), username);
//            client.sendMessageToServer("SIGNUP");
//            client.sendMessageToServer(username);
//            client.sendMessageToServer(password);
//
//            String response = client.receiveMessage();
//            if (response.contains("Registration complete")) {
//                System.out.println(response);
//                showLoginForm();
//            } else {
//                System.out.println(response);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @FXML
//    public void handleSendMessage() {
//        if (!isAuthenticated) {
//            System.out.println("You must log in first!");
//            return;
//        }
//
//        String messageToSend = tf_message.getText().trim();
//        if (!messageToSend.isEmpty()) {
//            try {
//                // Send the message to the server
//                client.sendMessageToServer(messageToSend);
//
//                // Display the message in the chat window (client-side)
//                HBox hBox = new HBox();
//                hBox.setAlignment(Pos.CENTER_RIGHT);
//                hBox.setPadding(new Insets(5, 5, 5, 10));
//
//                Text text = new Text("You: " + messageToSend);
//                TextFlow textFlow = new TextFlow(text);
//                textFlow.setStyle("-fx-background-color: rgb(15,215,242); -fx-background-radius: 20px;");
//                textFlow.setPadding(new Insets(10));
//                text.setFill(Color.WHITE);
//
//                hBox.getChildren().add(textFlow);
//                Platform.runLater(() -> vbox_message.getChildren().add(hBox));
//
//                tf_message.clear(); // Clear the message input field
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println("Failed to send message.");
//            }
//        }
//    }
//
//
//    @FXML
//    public void showSignUpForm() {
//        try {
//            Parent signUpRoot = FXMLLoader.load(getClass().getResource("/com/projex/javafx_chat/client/sign_up.fxml"));
//            Stage stage = (Stage) tf_username.getScene().getWindow();
//            stage.setScene(new Scene(signUpRoot, 600, 400));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @FXML
//    public void showLoginForm() {
//        try {
//            Parent loginRoot = FXMLLoader.load(getClass().getResource("/com/projex/javafx_chat/client/client.fxml"));
//            Stage stage = (Stage) tf_newUsername.getScene().getWindow();
//            stage.setScene(new Scene(loginRoot, 600, 400));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void enableChatInterface() {
//        tf_message.setDisable(false);
//        button_send.setDisable(false);
//    }
//
//    public static void addLabel(String message, VBox vBox) {
//        HBox hBox = new HBox();
//        hBox.setAlignment(Pos.CENTER_LEFT);
//        hBox.setPadding(new Insets(5, 5, 5, 10));
//
//        Text text = new Text(message);
//        TextFlow textFlow = new TextFlow(text);
//        textFlow.setStyle("-fx-background-color: rgb(233,233,235); -fx-background-radius: 20px;");
//        textFlow.setPadding(new Insets(10));
//
//        hBox.getChildren().add(textFlow);
//        Platform.runLater(() -> vBox.getChildren().add(hBox));
//    }
//}

//----------------------------------------------------------------------------------------------------------------------

package com.projex.javafx_chat.client;

import com.projex.javafx_chat.shared.DatabaseUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    @FXML private TextField tfusername;
    @FXML private PasswordField pfpassword;
    @FXML private ScrollPane spmain;
    @FXML private VBox vboxmessage;
    @FXML private TextField tfmessage;
    @FXML private Button buttonsend;
    @FXML private VBox chatSection;
    @FXML private VBox loginSection;

    private Client client;



    @FXML
    public void handleLogin() {
        String username = tfusername.getText();
        String password = pfpassword.getText();

        if (DatabaseUtil.authenticateUser(username, password)) {
            initializeClientConnection(username);
        } else {
            showAlert("Login Failed", "Invalid username or password");
        }
    }

    @FXML
    private void handleLogout() {
        if (client != null) {
            try {
                // Close all client resources
                if (client.getSocket() != null) {
                    client.getSocket().close();
                }

                // Show success alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Logout");
                alert.setHeaderText(null);
                alert.setContentText("Logged out successfully!");
                alert.showAndWait();

                // Close the window
                Stage stage = (Stage) tfusername.getScene().getWindow();
                stage.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private void initializeClientConnection(String username) {
        client = new Client(username);
        client.setMessageCallback(this::displayMessage);

        loginSection.setVisible(false);
        chatSection.setVisible(true);

        tfmessage.setDisable(false);
        buttonsend.setDisable(false);
    }

    @FXML
    public void handleSendMessage() {
        String message = tfmessage.getText();
        if (client != null && !message.isEmpty()) {
            client.sendMessage(message);
            tfmessage.clear();
        }
    }

    private void displayMessage(String message) {
        Platform.runLater(() -> {
            Label messageLabel = new Label(message);
            vboxmessage.getChildren().add(messageLabel);
        });
    }

    @FXML
    public void showSignUpForm() {
        try {
            Parent signUpRoot = FXMLLoader.load(getClass().getResource("/com/projex/javafx_chat/client/sign_up.fxml"));
            Stage stage = (Stage) tfusername.getScene().getWindow();
            stage.setScene(new Scene(signUpRoot));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
