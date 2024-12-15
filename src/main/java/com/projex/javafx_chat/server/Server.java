//package com.projex.javafx_chat.server;
//
//import com.projex.javafx_chat.shared.DatabaseSetup;
//import javafx.application.Platform;
//
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Server {
//
//    private ServerSocket serverSocket;
//    private Controller controller; // Reference to the server controller
//    private List<ClientHandler> clientHandlers = new ArrayList<>();
//    private boolean isRunning = false;
//
//    public Server(ServerSocket serverSocket, Controller controller) {
//        this.serverSocket = serverSocket;
//        this.controller = controller;
//
//        // Initialize the database
//        DatabaseSetup.initializeDatabase();
//    }
//
//    public void start() {
//        Platform.runLater(() -> controller.logMessage("Server is starting..."));
//        isRunning = true;
//
//        try {
//
//
//            // Accept client connections
//            while (!serverSocket.isClosed() && isRunning) {
//                Socket socket = serverSocket.accept();
//                Platform.runLater(() -> controller.logMessage("A new client has connected!"));
//
//                ClientHandler clientHandler = new ClientHandler(socket, this);
//                clientHandlers.add(clientHandler);
//
//                Thread thread = new Thread(clientHandler);
//                thread.start();
//            }
//        } catch (IOException e) {
//            if (isRunning){
//                Platform.runLater(() -> controller.logMessage("Error: " + e.getMessage()));
//            }
//        }
//    }
//
//    public void stop(){
//        try {
//            isRunning = false;
//
//            for(ClientHandler clientHandler : clientHandlers){
//                clientHandler.closeEverything(clientHandler.getSocket(), clientHandler.getBufferedReader(), clientHandler.getBufferedWriter());
//            }
//            clientHandlers.clear();
//
//            if(serverSocket != null && !serverSocket.isClosed()){
//                serverSocket.close();
//            }
//
//            Platform.runLater(() -> controller.logMessage("Server stopped successfully"));
//        } catch (IOException e) {
//            Platform.runLater(() -> controller.logMessage("Error stopping server: "+ e.getMessage()));
//        }
//    }
//
//    public void broadcastMessage(String message, ClientHandler sender) {
//        for (ClientHandler clientHandler : clientHandlers) {
//            if (clientHandler != sender) {
//                clientHandler.sendMessage(message);
//            }
//        }
//    }
//
//    public void removeClientHandler(ClientHandler clientHandler) {
//        clientHandlers.remove(clientHandler);
//    }
//
//    // Add a public getter for the controller
//    public Controller getController() {
//        return controller;
//    }
//}

//------------------------------------------------------------------------------------------------------

package com.projex.javafx_chat.server;

import com.projex.javafx_chat.shared.DatabaseSetup;
import javafx.application.Platform;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private ServerSocket serverSocket;
    private Controller controller;
    private List<ClientHandler> clientHandlers = new ArrayList<>();
    private boolean isRunning = false;

    public Server(ServerSocket serverSocket, Controller controller) {
        this.serverSocket = serverSocket;
        this.controller = controller;
        DatabaseSetup.initializeDatabase();
    }

    public void start() {
        Platform.runLater(() -> controller.logMessage("Server is starting..."));
        isRunning = true;

        try {
            while (!serverSocket.isClosed() && isRunning) {
                Socket socket = serverSocket.accept();
                Platform.runLater(() -> controller.logMessage("A new client has connected!"));

                ClientHandler clientHandler = new ClientHandler(socket, this);
                clientHandlers.add(clientHandler);

                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            if (isRunning) {
                Platform.runLater(() -> controller.logMessage("Server error: " + e.getMessage()));
            }
        }
    }

    public void stop() {
        try {
            isRunning = false;

            for (ClientHandler clientHandler : clientHandlers) {
                clientHandler.closeEverything();
            }
            clientHandlers.clear();

            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }

            Platform.runLater(() -> controller.logMessage("Server stopped successfully."));
        } catch (IOException e) {
            Platform.runLater(() -> controller.logMessage("Error stopping server: " + e.getMessage()));
        }
    }

    public void broadcastMessage(String message, ClientHandler sender) {
        System.out.println("DEBUG: Broadcasting message: " + message);
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler != sender) {
                clientHandler.sendMessage(message);
            }
        }
    }


    public void removeClientHandler(ClientHandler clientHandler) {
        if (clientHandlers.remove(clientHandler)) {
            System.out.println("DEBUG: ClientHandler removed for user: " + clientHandler.getUsername());
        } else {
            System.out.println("DEBUG: ClientHandler not found in the list.");
        }
    }





    public Controller getController() {
        return controller;
    }
}
