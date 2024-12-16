//package com.projex.javafx_chat.server;
//
//import com.projex.javafx_chat.shared.DatabaseUtil;
//
//import java.io.*;
//import java.net.Socket;
//
//public class ClientHandler implements Runnable {
//
//    private Socket socket;
//    private BufferedReader bufferedReader;
//    private BufferedWriter bufferedWriter;
//    private String username;
//    private Server server;
//
//    public ClientHandler(Socket socket, Server server) {
//        try {
//            this.socket = socket;
//            this.server = server;
//            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//
//            authenticateClient();
//
//        } catch (IOException e) {
//            closeEverything(socket, bufferedReader, bufferedWriter);
//        }
//    }
//
//    private void authenticateClient() throws IOException {
//        while (true) {
//            String username = bufferedReader.readLine();
//            String action = bufferedReader.readLine();
//            String password = bufferedReader.readLine();
//
//            if ("SIGNUP".equals(action)) {
//                if (DatabaseUtil.isUserExists(username)) {
//                    sendMessage("Username already exists.");
//                } else {
//                    DatabaseUtil.registerUser(username, password);
//                    sendMessage("Registration Complete");
//                }
//            } else if ("LOGIN".equals(action)) {
//                if (DatabaseUtil.authenticateUser(username, password)) {
//                    sendMessage("Login successful");
//                    break;
//                } else {
//                    sendMessage("Invalid credentials");
//                }
//            }
//        }
//    }
//
//    @Override
//    public void run() {
//        String messageFromClient;
//
//        while (socket.isConnected()) {
//            try {
//                messageFromClient = bufferedReader.readLine();
//
//                if (messageFromClient != null) {
//                    server.getController().logMessage("Message from " + username + ": " + messageFromClient);
//                    DatabaseUtil.saveChat(username, messageFromClient);
//                    server.broadcastMessage(username + ": " + messageFromClient, this);
//                }
//            } catch (IOException e) {
//                closeEverything(socket, bufferedReader, bufferedWriter);
//                break;
//            }
//        }
//    }
//
//    public void sendMessage(String message) {
//        try {
//            bufferedWriter.write(message);
//            bufferedWriter.newLine();
//            bufferedWriter.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
//        server.removeClientHandler(this);
//        try {
//            if (bufferedReader != null) bufferedReader.close();
//            if (bufferedWriter != null) bufferedWriter.close();
//            if (socket != null) socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
//----------------------------------------------------------------------------------------------
//package com.projex.javafx_chat.server;
//
//import com.projex.javafx_chat.shared.DatabaseUtil;
//
//import java.io.*;
//import java.net.Socket;
//
//public class ClientHandler implements Runnable {
//
//    private Socket socket;
//    private Server server;
//    private BufferedReader bufferedReader;
//    private BufferedWriter bufferedWriter;
//    private String username;
//    // Reference to server controller
//
//
//    public Socket getSocket(){
//        return socket;
//    }
//
//    public BufferedWriter getBufferedWriter(){
//        return bufferedWriter;
//    }
//
//    public BufferedReader getBufferedReader(){
//        return bufferedReader;
//    }
//
//    public ClientHandler(Socket socket, Server server) {
//        try {
//            this.socket = socket;
//            this.server = server;
//            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//
//            authenticateClient(); // Authenticate client during initialization
//        } catch (IOException e) {
//            closeEverything(socket, bufferedReader, bufferedWriter);
//        }
//    }
//
//    /**
//     * Authenticates the client, either via login or signup.
//     *
//     * @throws IOException if input/output fails during authentication.
//     */
//    private void authenticateClient() throws IOException {
//        while (true) {
//            String username = bufferedReader.readLine();
//            String action = bufferedReader.readLine(); // "LOGIN" or "SIGNUP"
//            String password = bufferedReader.readLine();
//
//            if ("SIGNUP".equals(action)) {
//                if (DatabaseUtil.isUserExists(username)) {
//                    sendMessage("Username already exists.");
//                } else {
//                    DatabaseUtil.registerUser(username, password);
//                    sendMessage("Registration complete");
//                }
//            } else if ("LOGIN".equals(action)) {
//                if (DatabaseUtil.authenticateUser(username, password)) {
//                    sendMessage("Login successful");
//                    this.username = username;
//                    server.getController().addUserToComboBox(username); // Add user to the ComboBox
//                    break;
//                } else {
//                    sendMessage("Invalid credentials");
//                }
//            }
//        }
//    }
//
//    @Override
//    public void run() {
//        String messageFromClient;
//
//        while (socket.isConnected()) {
//            try {
//                messageFromClient = bufferedReader.readLine();
//
//                if (messageFromClient != null) {
//                    // Log the message in the server logs
//                    server.getController().logMessage("Message from " + username + ": " + messageFromClient);
//
//                    // Save the message to the database
//                    DatabaseUtil.saveChat(username, messageFromClient);
//
//                    // Broadcast the message to all connected clients
//                    broadcastMessageToOtherClients(messageFromClient);
//                }
//            } catch (IOException e) {
//                closeEverything(socket, bufferedReader, bufferedWriter);
//                break;
//            }
//        }
//    }
//
//    /**
//     * Sends a message to the client.
//     *
//     * @param message The message to send.
//     */
//    public void sendMessage(String message) {
//        try {
//            bufferedWriter.write(message);
//            bufferedWriter.newLine();
//            bufferedWriter.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Broadcasts the message to other connected clients.
//     *
//     * @param message The message to broadcast.
//     */
//    private void broadcastMessageToOtherClients(String message) {
//        for (ClientHandler clientHandler : server.getController().getClientHandlers()) {
//            if (!clientHandler.equals(this)) {
//                clientHandler.sendMessage(username + ": " + message);
//            }
//        }
//    }
//
//    /**
//     * Closes the connections and removes the client handler from the server.
//     *
//     * @param socket         The socket to close.
//     * @param bufferedReader The bufferedReader to close.
//     * @param bufferedWriter The bufferedWriter to close.
//     */
//    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
//        server.getController().removeUserFromComboBox(username); // Remove user from ComboBox
//        server.getController().getClientHandlers().remove(this); // Remove the client handler
//        try {
//            if (bufferedReader != null) bufferedReader.close();
//            if (bufferedWriter != null) bufferedWriter.close();
//            if (socket != null) socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Gets the username of the connected client.
//     *
//     * @return The username.
//     */
//    public String getUsername() {
//        return username;
//    }
//}

//---------------------------------------------------------works-------------------------------------------------------------

//package com.projex.javafx_chat.server;
//
//import com.projex.javafx_chat.shared.DatabaseUtil;
//
//import java.io.*;
//import java.net.Socket;
//
//public class ClientHandler implements Runnable {
//
//    private Socket socket;
//    private Server server;
//    private BufferedReader bufferedReader;
//    private BufferedWriter bufferedWriter;
//    private String username;
//
//    public ClientHandler(Socket socket, Server server) {
//        try {
//            this.socket = socket;
//            this.server = server;
//            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//            authenticateClient();
//        } catch (IOException e) {
//            System.out.println("DEBUG: Exception during client handler initialization.");
//            closeEverything();
//        }
//    }
//
//    private void authenticateClient() throws IOException {
//
//        while (socket.isConnected()) {
//            try {
//                String action = bufferedReader.readLine();
//                if (action == null) break; // Exit if no action is received
//
//                String username = bufferedReader.readLine();
//                if (username == null) break;
//
//                String password = bufferedReader.readLine();
//                if (password == null) break;
//
//                if ("LOGIN".equals(action)) {
//                    if (DatabaseUtil.authenticateUser(username, password)) {
//                        sendMessage("Login successful");
//                        this.username = username;
//                        break;
//                    } else {
//                        sendMessage("Invalid credentials");
//                    }
//                } else if ("SIGNUP".equals(action)) {
//                    if (DatabaseUtil.isUserExists(username)) {
//                        sendMessage("Username already exists.");
//                    } else {
//                        DatabaseUtil.registerUser(username, password);
//                        sendMessage("Registration complete");
//                        server.getController().loadUsersIntoComboBox();
//                        break; // Exit after successful registration
//                    }
//                }
//            } catch (IOException e) {
//                System.out.println("Authentication error");
//                break;
//            }
//        }
//    }
//
//
//
//    @Override
//    public void run() {
//        String messageFromClient;
//
//        try {
//            while (true) {
//                messageFromClient = bufferedReader.readLine();
//
//                if (messageFromClient == null) {
//                    System.out.println("DEBUG: Null message received from client. Disconnecting...");
//                    break; // Exit loop when client disconnects
//                }
//
//                System.out.println("DEBUG: Message from " + username + ": " + messageFromClient);
//                DatabaseUtil.saveChat(username, messageFromClient);
//                server.broadcastMessage(username + ": " + messageFromClient, this);
//            }
//        } catch (IOException e) {
//            System.out.println("DEBUG: Exception in client handler for user: " + username);
//        } finally {
//            System.out.println("DEBUG: Cleaning up resources for user: " + username);
//            closeEverything();
//        }
//    }
//
//    public void sendMessage(String message) {
//        try {
//            bufferedWriter.write(message);
//            bufferedWriter.newLine();
//            bufferedWriter.flush();
//        } catch (IOException e) {
//            System.out.println("DEBUG: Error sending message to client: " + username);
//        }
//    }
//
//    public void closeEverything() {
//        try {
//            if (bufferedReader != null) bufferedReader.close();
//            if (bufferedWriter != null) bufferedWriter.close();
//            if (socket != null) socket.close();
//            System.out.println("DEBUG: Socket and streams closed for user: " + username);
//        } catch (IOException e) {
//            System.out.println("DEBUG: Error closing resources for user: " + username);
//        } finally {
//            server.removeClientHandler(this); // Remove client handler from server
//        }
//    }
//
//    public String getUsername() {
//        return username;
//    }
//}

//----------------------------------------------------------------------------------------------------------------------

package com.projex.javafx_chat.server;

import com.projex.javafx_chat.shared.DatabaseUtil;
import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private Server server;
    private BufferedReader in;
    private PrintWriter out;
    private String username;

    public String getUsername(){
        return this.username;
    }

    public ClientHandler(Socket socket, Server server) {
        try {
            this.socket = socket;
            this.server = server;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            closeEverything();
        }
    }

    @Override
    public void run() {
        try {
            // First message is always username
            username = in.readLine();
            server.getController().refreshUserList();
            server.getController().logMessage("New client connected: " + username);
            server.getController().addClientToList(username);

            String message;
            while ((message = in.readLine()) != null) {
                // Save message to database
                DatabaseUtil.saveChat(username, message);

                // Broadcast to other clients
                server.broadcastMessage(username + ": " + message, this);
            }
        } catch (IOException e) {
            closeEverything();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void closeEverything() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();

            server.getController().removeClientFromList(username);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.removeClient(this);
    }
}
