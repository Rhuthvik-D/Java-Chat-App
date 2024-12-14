package com.projex.javafx_chat.shared;

import java.sql.*;

public class DatabaseUtil {

    private static final String DB_URL = "jdbc:sqlite:src/main/resources/database/chatapp.db";

    /**
     * Establishes a connection to the SQLite database.
     *
     * @return Connection object for the database.
     * @throws SQLException if the connection fails.
     */
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Authenticates a user by verifying the username and password in the database.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return True if the credentials are valid, false otherwise.
     */
    public static boolean authenticateUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            // Execute the query and check if a result exists
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Checks if a user exists in the database by username.
     *
     * @param username The username to check.
     * @return True if the user exists, false otherwise.
     */
    public static boolean isUserExists(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            return stmt.executeQuery().next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Saves a chat message to the database.
     *
     * @param username The username of the sender.
     * @param message  The message content.
     */
    public static void saveChat(String username, String message) {
        String query = "INSERT INTO chats (username, message) VALUES (?, ?)";
        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, message);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all chat messages for a specific user.
     *
     * @param username The username whose chat messages are to be retrieved.
     * @return A ResultSet containing the user's chat messages.
     */
    public static ResultSet getChats(String username) {
        String query = "SELECT message, timestamp FROM chats WHERE username = ? ORDER BY timestamp ASC";
        try {
            Connection connection = connect();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all users from the database.
     *
     * @return A ResultSet containing all usernames in the database.
     */
    public static ResultSet getAllUsers() {
        String query = "SELECT username FROM users";
        try {
            Connection connection = connect();
            PreparedStatement stmt = connection.prepareStatement(query);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
