//package com.projex.javafx_chat.server;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//import java.net.ServerSocket;
//
//public class Main extends Application {
//
//    private static final int PORT = 1234;
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        // Load the server UI (server.fxml)
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/projex/javafx_chat/server/server.fxml"));
//        Parent root = loader.load();
//
//        // Get the server controller from the FXML loader
//        Controller controller = loader.getController();
//
//        // Start the server in a separate thread
//        new Thread(() -> {
//            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
//                Server server = new Server(serverSocket, controller);
//                server.start();
//            } catch (Exception e) {
//                e.printStackTrace();
//                controller.logMessage("Error starting server: " + e.getMessage());
//            }
//        }).start();
//
//        // Set up the server UI
//        primaryStage.setTitle("Server Dashboard");
//        primaryStage.setScene(new Scene(root, 600, 500));
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args); // Start the JavaFX application
//    }
//}

//----------------------------------------------------------------------------------------------------------------------

package com.projex.javafx_chat.server;

import com.projex.javafx_chat.shared.DatabaseSetup;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.ServerSocket;

public class Main extends Application {

    private static final int PORT = 1234;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/projex/javafx_chat/server/server.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();

        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                Server server = new Server(serverSocket, controller);
                server.start();
            } catch (Exception e) {
                e.printStackTrace();
                controller.logMessage("Error starting server: " + e.getMessage());
            }
        }).start();

        primaryStage.setTitle("Server Dashboard");
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        DatabaseSetup.initializeDatabase();
        launch(args);
    }
}

