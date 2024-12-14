package com.projex.javafx_chat.server;

import com.projex.javafx_chat.shared.DatabaseUtil;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private Server server;

    public ClientHandler(Socket socket, Server server) {
        try {
            this.socket = socket;
            this.server = server;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            authenticateClient();

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    private void authenticateClient() throws IOException {
        while (true) {
            String username = bufferedReader.readLine();

            if (DatabaseUtil.isUserExists(username)) {
                this.username = username;
                sendMessage("Login successful! Welcome, " + username);
                server.getController().logMessage("User authenticated: " + username);
                break;
            } else {
                sendMessage("Invalid username. Please try again.");
            }
        }
    }

    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();

                if (messageFromClient != null) {
                    server.getController().logMessage("Message from " + username + ": " + messageFromClient);
                    DatabaseUtil.saveChat(username, messageFromClient);
                    server.broadcastMessage(username + ": " + messageFromClient, this);
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void sendMessage(String message) {
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        server.removeClientHandler(this);
        try {
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
