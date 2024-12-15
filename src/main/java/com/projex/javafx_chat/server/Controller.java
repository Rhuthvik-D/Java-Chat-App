//package com.projex.javafx_chat.server;
//
//import com.projex.javafx_chat.shared.DatabaseUtil;
//import javafx.application.Platform;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.TextArea;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class Controller {
//
//    @FXML
//    private TextArea ta_Logs;
//
//    @FXML
//    private ComboBox<String> cb_users;
//
//    @FXML
//    private Button button_retrieveChat;
//
//    private ObservableList<String> usersList = FXCollections.observableArrayList();
//
//    public void initialize() {
//        loadUsersIntoComboBox();
//        button_retrieveChat.setOnAction(event -> retrieveChat());
//    }
//
//    public void logMessage(String message) {
//        Platform.runLater(() -> ta_Logs.appendText(message + "\n"));
//    }
//
//    private void loadUsersIntoComboBox() {
//        Platform.runLater(() -> {
//            try {
//                ResultSet rs = DatabaseUtil.getAllUsers();
//                while (rs != null && rs.next()) {
//                    usersList.add(rs.getString("username"));
//                }
//                cb_users.setItems(usersList);
//            } catch (SQLException e) {
//                logMessage("Error loading users: " + e.getMessage());
//            }
//        });
//    }
//
//    private void retrieveChat() {
//        String selectedUser = cb_users.getSelectionModel().getSelectedItem();
//        if (selectedUser == null || selectedUser.isEmpty()) {
//            logMessage("No user selected for chat retrieval.");
//            return;
//        }
//
//        try {
//            ResultSet rs = DatabaseUtil.getChats(selectedUser);
//            StringBuilder chatHistory = new StringBuilder("Chat History for " + selectedUser + ":\n");
//            while (rs != null && rs.next()) {
//                chatHistory.append(rs.getString("timestamp")).append(" - ")
//                        .append(rs.getString("message")).append("\n");
//            }
//            logMessage(chatHistory.toString());
//        } catch (SQLException e) {
//            logMessage("Error retrieving chat history for " + selectedUser + ": " + e.getMessage());
//        }
//    }
//}
//
//
//------------------------------------------------------------------
//package com.projex.javafx_chat.server;
//
//import com.projex.javafx_chat.shared.DatabaseUtil;
//import javafx.application.Platform;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.TextArea;
//
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Controller {
//
//    @FXML
//    private TextArea ta_logs; // Logs TextArea for displaying server logs
//
//    @FXML
//    private ComboBox<String> cb_users; // ComboBox for displaying connected users
//
//    @FXML
//    private Button button_retrieveChat; // Button to retrieve chat history
//    private Server server;
//
//
//    private ObservableList<String> usersList = FXCollections.observableArrayList();
//
//    private ServerSocket serverSocket;
//    private List<ClientHandler> clientHandlers = new ArrayList<>();
//    public List<ClientHandler> getClientHandlers(){
//        return clientHandlers;
//    }
//    private boolean isServerRunning = false; // Flag to track server state
//    private Thread serverThread;
//
//    /**
//     * Initializes the controller.
//     */
//    public void initialize() {
//        loadUsersIntoComboBox();
//        button_retrieveChat.setOnAction(event -> handleRetrieveChat());
//    }
//
//    /**
//     * Logs a message to the server logs TextArea.
//     *
//     * @param message The message to log.
//     */
//    public void logMessage(String message) {
//        Platform.runLater(() -> ta_logs.appendText(message + "\n"));
//    }
//
//    /**
//     * Starts the server and begins accepting client connections.
//     */
//    @FXML
//    private void handleStartServer() {
//        if (isServerRunning) {
//            logMessage("Server is already running!");
//            return;
//        }
//
//        logMessage("Starting server...");
//        try {
//            server = new Server(new ServerSocket(1234), this);
//            new Thread(() -> server.start()).start();
//            isServerRunning = true;
//            logMessage("Server started successfully");
//        } catch (Exception e) {
//            logMessage("Error starting server: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Stops the server and disconnects all clients.
//     */
//    @FXML
//    private void handleStopServer() {
//        if (!isServerRunning) {
//            logMessage("Server is not running!");
//            return;
//        }
//
//
//
//        logMessage("Stopping server...");
//
//        for (ClientHandler clientHandler : clientHandlers) {
//            clientHandler.closeEverything(clientHandler.getSocket(), clientHandler.getBufferedReader(), clientHandler.getBufferedWriter());
//        }
//        clientHandlers.clear();
//        server.stop();
//        isServerRunning = false;
//        logMessage("Server Stopped successfully");
//
//
//    }
//
//    /**
//     * Loads all registered users into the ComboBox from the database.
//     */
//    private void loadUsersIntoComboBox() {
//        Platform.runLater(() -> {
//            try {
//                ResultSet rs = DatabaseUtil.getAllUsers();
//                usersList.clear();
//                while (rs != null && rs.next()) {
//                    usersList.add(rs.getString("username"));
//                }
//                cb_users.setItems(usersList);
//            } catch (SQLException e) {
//                logMessage("Error loading users: " + e.getMessage());
//            }
//        });
//    }
//
//    /**
//     * Retrieves and displays the chat history for the selected user.
//     */
//    @FXML
//    private void handleRetrieveChat() {
//        String selectedUser = cb_users.getSelectionModel().getSelectedItem();
//        if (selectedUser == null || selectedUser.isEmpty()) {
//            logMessage("No user selected for chat retrieval.");
//            return;
//        }
//
//        try {
//            ResultSet rs = DatabaseUtil.getChats(selectedUser);
//            StringBuilder chatHistory = new StringBuilder("Chat History for " + selectedUser + ":\n");
//            while (rs != null && rs.next()) {
//                chatHistory.append(rs.getString("timestamp")).append(" - ")
//                        .append(rs.getString("message")).append("\n");
//            }
//            logMessage(chatHistory.toString());
//        } catch (SQLException e) {
//            logMessage("Error retrieving chat history for " + selectedUser + ": " + e.getMessage());
//        }
//    }
//
//    /**
//     * Adds a user to the ComboBox when they register or log in.
//     *
//     * @param username The username to add.
//     */
//    public void addUserToComboBox(String username) {
//        Platform.runLater(() -> {
//            if (!usersList.contains(username)) {
//                usersList.add(username);
//                cb_users.setItems(usersList);
//            }
//        });
//    }
//
//    /**
//     * Removes a user from the ComboBox when they disconnect.
//     *
//     * @param username The username to remove.
//     */
//    public void removeUserFromComboBox(String username) {
//        Platform.runLater(() -> {
//            usersList.remove(username);
//            cb_users.setItems(usersList);
//        });
//    }
//}


//----------------------------------------------------------------------------------------------

package com.projex.javafx_chat.server;

import com.projex.javafx_chat.shared.DatabaseUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.net.ServerSocket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Controller {

    @FXML
    private TextArea ta_logs;

    @FXML
    private ComboBox<String> cb_users;

    @FXML
    private Button button_retrieveChat;

    private Server server;
    private boolean isServerRunning = false;
    private ObservableList<String> usersList = FXCollections.observableArrayList();

    public void initialize() {
        loadUsersIntoComboBox();
        button_retrieveChat.setOnAction(event -> handleRetrieveChat());
    }

    @FXML
    public void handleStartServer() {
        if (isServerRunning) {
            logMessage("Server is already running!");
            return;
        }

        logMessage("Starting server...");
        try {
            server = new Server(new ServerSocket(1234), this);
            new Thread(() -> server.start()).start();
            isServerRunning = true;
            logMessage("Server started successfully.");
        } catch (Exception e) {
            logMessage("Error starting server: " + e.getMessage());
        }
    }

    @FXML
    public void handleStopServer() {
        if (!isServerRunning) {
            logMessage("Server is not running!");
            return;
        }

        logMessage("Stopping server...");
        server.stop();
        isServerRunning = false;
        logMessage("Server stopped successfully.");
    }

    public void loadUsersIntoComboBox() {
        Platform.runLater(() -> {
            try {
                // Fetch all users from the database
                ResultSet rs = DatabaseUtil.getAllUsers();
                usersList.clear(); // Clear the existing list

                while (rs != null && rs.next()) {
                    usersList.add(rs.getString("username")); // Add usernames to the list
                }

                cb_users.setItems(usersList); // Populate the ComboBox
                logMessage("Users loaded into ComboBox successfully.");
            } catch (SQLException e) {
                logMessage("Error loading users: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleRetrieveChat() {
        String selectedUser = cb_users.getSelectionModel().getSelectedItem();
        if (selectedUser == null || selectedUser.isEmpty()) {
            logMessage("No user selected for chat retrieval.");
            return;
        }

        try {
            ResultSet rs = DatabaseUtil.getChats(selectedUser);
            StringBuilder chatHistory = new StringBuilder("Chat History for " + selectedUser + ":\n");
            while (rs != null && rs.next()) {
                chatHistory.append(rs.getString("timestamp")).append(" - ")
                        .append(rs.getString("message")).append("\n");
            }
            logMessage(chatHistory.toString());
        } catch (SQLException e) {
            logMessage("Error retrieving chat history for " + selectedUser + ": " + e.getMessage());
        }
    }

    public void logMessage(String message) {
        Platform.runLater(() -> ta_logs.appendText(message + "\n"));
    }
}
