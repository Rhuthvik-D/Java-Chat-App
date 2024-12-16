//------------------------------------------------------works-----------------------------------------------------------

//package com.projex.javafx_chat.client;
//
//import javafx.application.Platform;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.PasswordField;
//import javafx.scene.control.TextField;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//import java.net.Socket;
//
//public class SignUpController {
//
//    @FXML
//    private TextField tf_newUsername;
//
//    @FXML
//    private PasswordField pf_newPassword;
//
//    @FXML
//    public void handleSignUp() {
//        String username = tf_newUsername.getText().trim();
//        String password = pf_newPassword.getText().trim();
//
//        if (username.isEmpty() || password.isEmpty()) {
//            System.out.println("DEBUG: Sign-up fields are empty. Aborting...");
//            return;
//        }
//
//        Client client = null;
//        try {
//            client = new Client(new Socket("localhost", 1234), username);
//            client.sendMessageToServer("SIGNUP");
//            client.sendMessageToServer(username);
//            client.sendMessageToServer(password);
//
//            String response = client.receiveMessage();
//            System.out.println("DEBUG: Server Response: " + response);
//
//            if (response.contains("Registration complete")) {
//                System.out.println("DEBUG: Registration successful. Returning to login.");
//            } else {
//                System.out.println("DEBUG: Registration failed: " + response);
//                return;
//            }
//        } catch (IOException e) {
//            System.out.println("DEBUG: Exception during registration.");
//            e.printStackTrace();
//        } finally {
//            if (client != null) {
//                client.closeEverything(client.getSocket(), client.getBufferedReader(), client.getBufferedWriter());
//                System.out.println("DEBUG: Client socket closed after registration.");
//            }
//        }
//
//        // Navigate back to the login screen
//        Platform.runLater(this::showLoginForm);
//    }
//
//    @FXML
//    public void showLoginForm() {
//        try {
//            Parent loginRoot = FXMLLoader.load(getClass().getResource("/com/projex/javafx_chat/client/client.fxml"));
//            Stage stage = (Stage) tf_newUsername.getScene().getWindow();
//            stage.setScene(new Scene(loginRoot, 600, 400));
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}

//----------------------------------------------------------------------------------------------------------------------

package com.projex.javafx_chat.client;

import com.projex.javafx_chat.shared.DatabaseUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpController {
    @FXML private TextField tfnewUsername;
    @FXML private PasswordField pfnewPassword;

    private Runnable onUserRegistered;

    public void setOnUserRegistered(Runnable callback){
        this.onUserRegistered = callback;
    }

    @FXML
    public void handleSignUp() {
        String username = tfnewUsername.getText().trim();
        String password = pfnewPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Registration Error", "Username and password cannot be empty");
            return;
        }

        if (DatabaseUtil.isUserExists(username)) {
            showAlert("Registration Error", "Username already exists");
        } else if (DatabaseUtil.registerUser(username, password)){
            if (onUserRegistered != null){
                onUserRegistered.run();
            }
            showLoginForm();
        }
    }

    @FXML
    public void showLoginForm() {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/com/projex/javafx_chat/client/client.fxml"));
            Stage stage = (Stage) tfnewUsername.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
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
