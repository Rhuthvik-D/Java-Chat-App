//package com.projex.javafx_chat.shared;
//
//import java.sql.Connection;
//import java.sql.Statement;
//
//public class DatabaseSetup {
//
//    public static void initializeDatabase() {
//        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
//                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "username TEXT NOT NULL UNIQUE, " +
//                "password TEXT NOT NULL" +
//                ");";
//
//        String createChatsTable = "CREATE TABLE IF NOT EXISTS chats (" +
//                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "username TEXT NOT NULL, " +
//                "message TEXT NOT NULL, " +
//                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, " +
//                "FOREIGN KEY (username) REFERENCES users(username)" +
//                ");";
//
//        String insertSampleUsers = "INSERT OR IGNORE INTO users (username, password) VALUES " +
//                "('testuser', 'password123'), " +
//                "('johndoe', 'secret456');";
//
//        try (Connection connection = DatabaseUtil.connect();
//             Statement statement = connection.createStatement()) {
//            statement.executeUpdate(createUsersTable);
//            statement.executeUpdate(createChatsTable);
//            statement.executeUpdate(insertSampleUsers);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}

//----------------------------------------------------------------------------------------------------------------------

package com.projex.javafx_chat.shared;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseSetup {

    public static void initializeDatabase() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL" +
                ");";

        String createChatsTable = "CREATE TABLE IF NOT EXISTS chats (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL, " +
                "message TEXT NOT NULL, " +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (username) REFERENCES users(username)" +
                ");";

        String insertSampleUsers = "INSERT OR IGNORE INTO users (username, password) VALUES " +
                "('testuser', 'password123'), " +
                "('johndoe', 'secret456');";

        try (Connection connection = DatabaseUtil.connect();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(createUsersTable);
            statement.executeUpdate(createChatsTable);
            statement.executeUpdate(insertSampleUsers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

