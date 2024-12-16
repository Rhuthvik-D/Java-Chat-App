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


//-----------------------------------------------------------works------------------------------------------------------

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
//import java.net.ServerSocket;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class Controller {
//
//    @FXML
//    private TextArea ta_logs;
//
//    @FXML
//    private ComboBox<String> cb_users;
//
//    @FXML
//    private Button button_retrieveChat;
//
//    private Server server;
//    private boolean isServerRunning = false;
//    private ObservableList<String> usersList = FXCollections.observableArrayList();
//
//    public void initialize() {
//        loadUsersIntoComboBox();
//        button_retrieveChat.setOnAction(event -> handleRetrieveChat());
//    }
//
//    @FXML
//    public void handleStartServer() {
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
//            logMessage("Server started successfully.");
//        } catch (Exception e) {
//            logMessage("Error starting server: " + e.getMessage());
//        }
//    }
//
//    @FXML
//    public void handleStopServer() {
//        if (!isServerRunning) {
//            logMessage("Server is not running!");
//            return;
//        }
//
//        logMessage("Stopping server...");
//        server.stop();
//        isServerRunning = false;
//        logMessage("Server stopped successfully.");
//    }
//
//    public void loadUsersIntoComboBox() {
//        Platform.runLater(() -> {
//            try {
//                // Fetch all users from the database
//                ResultSet rs = DatabaseUtil.getAllUsers();
//                usersList.clear(); // Clear the existing list
//
//                while (rs != null && rs.next()) {
//                    usersList.add(rs.getString("username")); // Add usernames to the list
//                }
//
//                cb_users.setItems(usersList); // Populate the ComboBox
//                logMessage("Users loaded into ComboBox successfully.");
//            } catch (SQLException e) {
//                logMessage("Error loading users: " + e.getMessage());
//            }
//        });
//    }
//
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
//    public void logMessage(String message) {
//        Platform.runLater(() -> ta_logs.appendText(message + "\n"));
//    }
//}

//----------------------------------------------------------------------------------------------------------------------

package com.projex.javafx_chat.server;

import com.projex.javafx_chat.shared.DatabaseUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.xml.transform.Result;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Controller {
    @FXML private TextField portField;
    @FXML private ComboBox<String> userComboBox;
    @FXML private TextArea chatHistoryArea;
    @FXML private TextArea serverLogArea;
    @FXML private TextField tfmessage;
    @FXML private ListView<String> clientListView;
    private ObservableList<String> connectedClients = FXCollections.observableArrayList();
    private String selectedUser;
    private Server server;

    @FXML
    public void initialise(){
        loadUsers();
        loadUsersIntoComboBox();
    }

    public void setServer(Server server){
        this.server =  server;
    }

    private void loadUsers(){
        try {
            ResultSet rs = DatabaseUtil.getAllUsers();
            while (rs.next()){
                userComboBox.getItems().add(rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRetrieveChat(){
        selectedUser = userComboBox.getValue();
        if (selectedUser == null){
            logMessage("No user selected");
            return;
        }
        else if (selectedUser != null){
            try {
                ResultSet rs = DatabaseUtil.getChats(selectedUser);
                StringBuilder chatHistory= new StringBuilder();
                while (rs.next()){
                    chatHistory.append(rs.getString("timestamp"))
                            .append(" - ")
                            .append(rs.getString("message"))
                            .append("\n");
                }
                chatHistoryArea.setText(chatHistory.toString());
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    private void loadUsersIntoComboBox(){
        Platform.runLater(() -> {
            try{
                ResultSet rs = DatabaseUtil.getAllUsers();
                while(rs != null && rs.next()){
                    userComboBox.getItems().add(rs.getString("username"));
                }
            } catch (SQLException e) {
                logMessage("Error loading users: " + e.getMessage());

            }
        });
    }

    public void refreshUserList(){
        userComboBox.getItems().clear();
        loadUsersIntoComboBox();
    }

    private void sendChatHistory(int minutes){
        if(selectedUser == null || selectedUser.isEmpty()){
            logMessage("No user selected");
            return;
        } else if (server == null) {
            logMessage("Server is not initialised");
            return;
        }

        try {
            ResultSet rs;
            if (minutes > 0){
                rs = DatabaseUtil.getRecentChats(selectedUser, minutes);
            } else{
                rs = DatabaseUtil.getChats(selectedUser);
            }

            StringBuilder chatHistory = new StringBuilder();
            while(rs.next()){
                chatHistory.append(rs.getString("timestamp"))
                        .append(" - ")
                        .append(rs.getString("message"))
                        .append("\n");

            }
            for(ClientHandler handler : server.getClientHandlers()){
                if(handler.getUsername().equals(selectedUser)){
                    handler.sendMessage("CHAT HISTORY:" + chatHistory.toString());
                    break;
                }
            }
        } catch (SQLException e) {
            logMessage("Error retrieving chat history: " + e.getMessage());
        }
    }

    @FXML
    private void sendLast2MinChat(){
        sendChatHistory(2);
    }

    @FXML
    private void sendLast5MinChat(){
        sendChatHistory(5);
    }

    @FXML
    private void sendAllChat(){
        sendChatHistory(0);
    }


    @FXML
    public void startServer() {
        int port = Integer.parseInt(portField.getText());
        try {
            server = new Server(new ServerSocket(port), this);
            new Thread(server::start).start();
            logMessage("Server started on port " + port);
        } catch (IOException e) {
            logMessage("Error starting server: " + e.getMessage());
        }
    }

    @FXML
    public void stopServer() {
        if (server != null) {
            // Notify all clients to close
            for (ClientHandler clientHandler : server.getClientHandlers()) {
                try {
                    clientHandler.sendMessage("SERVER_SHUTDOWN");
                    clientHandler.closeEverything();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Close server socket
            server.closeServerSocket();
            logMessage("Server stopped");

            // Close server dashboard
            Platform.runLater(() -> {
                Stage stage = (Stage) tfmessage.getScene().getWindow();
                stage.close();
            });
        }
    }

    public void logMessage(String message) {
        Platform.runLater(() -> {
            serverLogArea.appendText(message + "\n");
        });
    }

    public void addClientToList(String username) {
        Platform.runLater(() -> {
            if (!connectedClients.contains(username)) {
                connectedClients.add(username);
                clientListView.setItems(connectedClients);
            }
        });
    }

    public void removeClientFromList(String username) {
        Platform.runLater(() -> {
            connectedClients.remove(username);
            clientListView.setItems(connectedClients);
        });
    }
}
