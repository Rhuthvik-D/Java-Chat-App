//package com.projex.javafx_chat.shared;
//
//import java.sql.*;
//
//public class DatabaseUtil {
//
//    private static final String DB_URL = "jdbc:sqlite:src/main/resources/database/chatapp.db";
//
//    /**
//     * Establishes a connection to the SQLite database.
//     *
//     * @return Connection object for the database.
//     * @throws SQLException if the connection fails.
//     */
//    public static Connection connect() throws SQLException {
//        return DriverManager.getConnection(DB_URL);
//    }
//
//    /**
//     * Authenticates a user by verifying the username and password in the database.
//     *
//     * @param username The username of the user.
//     * @param password The password of the user.
//     * @return True if the credentials are valid, false otherwise.
//     */
//    public static boolean authenticateUser(String username, String password) {
//        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
//        try (Connection connection = connect();
//             PreparedStatement stmt = connection.prepareStatement(query)) {
//
//            stmt.setString(1, username);
//            stmt.setString(2, password);
//
//            // Execute the query and check if a result exists
//            ResultSet rs = stmt.executeQuery();
//            return rs.next();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    /**
//     * Checks if a user exists in the database by username.
//     *
//     * @param username The username to check.
//     * @return True if the user exists, false otherwise.
//     */
//    public static boolean isUserExists(String username) {
//        String query = "SELECT * FROM users WHERE username = ?";
//        try (Connection connection = connect();
//             PreparedStatement stmt = connection.prepareStatement(query)) {
//
//            stmt.setString(1, username);
//            return stmt.executeQuery().next();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    /**
//     * Saves a chat message to the database.
//     *
//     * @param username The username of the sender.
//     * @param message  The message content.
//     */
//    public static void saveChat(String username, String message) {
//        String query = "INSERT INTO chats (username, message) VALUES (?, ?)";
//        try (Connection connection = connect();
//             PreparedStatement stmt = connection.prepareStatement(query)) {
//
//            stmt.setString(1, username);
//            stmt.setString(2, message);
//            stmt.executeUpdate();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Retrieves all chat messages for a specific user.
//     *
//     * @param username The username whose chat messages are to be retrieved.
//     * @return A ResultSet containing the user's chat messages.
//     */
//    public static ResultSet getChats(String username) {
//        String query = "SELECT message, timestamp FROM chats WHERE username = ? ORDER BY timestamp ASC";
//        try {
//            Connection connection = connect();
//            PreparedStatement stmt = connection.prepareStatement(query);
//            stmt.setString(1, username);
//            return stmt.executeQuery();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * Retrieves all users from the database.
//     *
//     * @return A ResultSet containing all usernames in the database.
//     */
//    public static ResultSet getAllUsers() {
//        String query = "SELECT username FROM users";
//        try {
//            Connection connection = connect();
//            PreparedStatement stmt = connection.prepareStatement(query);
//            return stmt.executeQuery();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static boolean registerUser(String username, String password){
//        String query = "INSERT INTO users (username, password) VALUES (?,?)";
//        try (Connection connection = connect();
//            PreparedStatement stmt = connection.prepareStatement(query)){
//            stmt.setString(1, username);
//            stmt.setString(2, password);
//            stmt.executeUpdate();
//            return true;
//        } catch (SQLException e){
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//}

//-----------------------------------------------works-----------------------------------------------------------------------
//package com.projex.javafx_chat.shared;
//
//import java.sql.*;
//
//public class DatabaseUtil {
//
//    private static final String DB_URL = "jdbc:sqlite:src/main/resources/database/chatapp.db";
//
//    public static Connection connect() throws SQLException {
//        return DriverManager.getConnection(DB_URL);
//    }
//
//    public static boolean authenticateUser(String username, String password) {
//        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
//        try (Connection connection = connect();
//             PreparedStatement stmt = connection.prepareStatement(query)) {
//
//            stmt.setString(1, username);
//            stmt.setString(2, password);
//            System.out.println("Executing query: " + stmt.toString()); // Debug query
//
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                System.out.println("Authentication successful for user: " + username);
//                return true;
//            } else {
//                System.out.println("Authentication failed for user: " + username);
//                return false;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//
//
//    public static boolean isUserExists(String username) {
//        String query = "SELECT * FROM users WHERE username = ?";
//        try (Connection connection = connect();
//             PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setString(1, username);
//            return stmt.executeQuery().next();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public static void saveChat(String username, String message) {
//        String query = "INSERT INTO chats (username, message) VALUES (?, ?)";
//        try (Connection connection = connect();
//             PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setString(1, username);
//            stmt.setString(2, message);
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static ResultSet getChats(String username) {
//        String query = "SELECT message, timestamp FROM chats WHERE username = ? ORDER BY timestamp ASC";
//        try {
//            Connection connection = connect();
//            PreparedStatement stmt = connection.prepareStatement(query);
//            stmt.setString(1, username);
//            return stmt.executeQuery();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static ResultSet getAllUsers() {
//        String query = "SELECT username FROM users";
//        try {
//            Connection connection = connect();
//            PreparedStatement stmt = connection.prepareStatement(query);
//            return stmt.executeQuery();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static boolean registerUser(String username, String password) {
//        String query = "INSERT INTO users (username, password) VALUES (?,?)";
//        try (Connection connection = connect();
//             PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setString(1, username);
//            stmt.setString(2, password);
//            stmt.executeUpdate();
//            return true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//}
//

//----------------------------------------------------------------------------------------------------------------------

package com.projex.javafx_chat.shared;

import java.sql.*;

public class DatabaseUtil {
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/database/chatapp.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static boolean authenticateUser(String username, String password) {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT * FROM users WHERE username = ? AND password = ?")) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            return pstmt.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isUserExists(String username) {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT * FROM users WHERE username = ?")) {
            pstmt.setString(1, username);
            return pstmt.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ResultSet getAllUsers() throws SQLException {
        Connection conn = connect();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery("SELECT username FROM users");
    }

    public static ResultSet getChats(String username) throws SQLException {
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(
                "SELECT timestamp, message FROM chats WHERE username = ? ORDER BY timestamp"
        );
        pstmt.setString(1, username);
        return pstmt.executeQuery();
    }

    public static boolean registerUser(String username, String password) {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO users (username, password) VALUES (?, ?)")) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ResultSet getRecentChats(String username, int minutes) throws SQLException{
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(
                "SELECT timestamp, message FROM chats " +
                    "WHERE username = ? AND timestamp >= datetime('now', '-' || ? || ' minute')" +
                    "ORDER BY timestamp DESC"
        );
        pstmt.setString(1, username);
        pstmt.setInt(2, minutes);
        return pstmt.executeQuery();
    }

    public static void saveChat(String username, String message) {
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO chats (username, message) VALUES (?, ?)")) {
            pstmt.setString(1, username);
            pstmt.setString(2, message);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
