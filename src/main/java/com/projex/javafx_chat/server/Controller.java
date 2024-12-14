package com.projex.javafx_chat.server;

import com.projex.javafx_chat.shared.DatabaseUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Controller {

    @FXML
    private TextArea ta_serverLogs;

    @FXML
    private ComboBox<String> cb_users;

    @FXML
    private Button button_retrieveChat;

    private ObservableList<String> usersList = FXCollections.observableArrayList();

    public void initialize() {
        loadUsersIntoComboBox();
        button_retrieveChat.setOnAction(event -> retrieveChat());
    }

    public void logMessage(String message) {
        Platform.runLater(() -> ta_serverLogs.appendText(message + "\n"));
    }

    private void loadUsersIntoComboBox() {
        Platform.runLater(() -> {
            try {
                ResultSet rs = DatabaseUtil.getAllUsers();
                while (rs != null && rs.next()) {
                    usersList.add(rs.getString("username"));
                }
                cb_users.setItems(usersList);
            } catch (SQLException e) {
                logMessage("Error loading users: " + e.getMessage());
            }
        });
    }

    private void retrieveChat() {
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
}
