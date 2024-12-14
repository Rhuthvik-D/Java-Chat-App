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
    private Controller controller; // Reference to the server controller
    private List<ClientHandler> clientHandlers = new ArrayList<>();

    public Server(ServerSocket serverSocket, Controller controller) {
        this.serverSocket = serverSocket;
        this.controller = controller;
    }

    public void start() {
        Platform.runLater(() -> controller.logMessage("Server is starting..."));

        try {
            // Initialize the database
            DatabaseSetup.initializeDatabase();

            // Accept client connections
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                Platform.runLater(() -> controller.logMessage("A new client has connected!"));

                ClientHandler clientHandler = new ClientHandler(socket, this);
                clientHandlers.add(clientHandler);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            Platform.runLater(() -> controller.logMessage("Error: " + e.getMessage()));
        }
    }

    public void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler != sender) {
                clientHandler.sendMessage(message);
            }
        }
    }

    public void removeClientHandler(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
    }

    // Add a public getter for the controller
    public Controller getController() {
        return controller;
    }
}
