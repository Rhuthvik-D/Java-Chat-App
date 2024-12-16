//package com.projex.javafx_chat.client;
//
//import javafx.scene.layout.VBox;
//
//import java.io.*;
//import java.net.Socket;
//
//public class Client {
//
//    private Socket socket;
//    private BufferedReader bufferedReader;
//    private BufferedWriter bufferedWriter;
//    private String username; // Track the client's username
//
//    public Client(Socket socket, String username) {
//        try {
//            this.socket = socket;
//            this.username = username; // Assign username to the client
//            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//
//            // Send the username to the server for identification
//            sendMessageToServer(username);
//
//        } catch (IOException e) {
//            System.out.println("Error creating client");
//            e.printStackTrace();
//            closeEverything(socket, bufferedReader, bufferedWriter);
//        }
//    }
//
//    public void sendMessageToServer(String messageToServer) {
//        try {
//            bufferedWriter.write(messageToServer);
//            bufferedWriter.newLine();
//            bufferedWriter.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("Error sending message to the server");
//            closeEverything(socket, bufferedReader, bufferedWriter);
//        }
//    }
//
//    public void receiveMessageFromServer(VBox vBox) {
//        new Thread(() -> {
//            while (socket.isConnected()) {
//                try {
//                    String messageFromServer = bufferedReader.readLine();
//                    Controller.addLabel(messageFromServer, vBox);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    System.out.println("Error receiving message from the server");
//                    closeEverything(socket, bufferedReader, bufferedWriter);
//                    break;
//                }
//            }
//        }).start();
//    }
//    public String receiveMessage(){
//        try {
//            return bufferedReader.readLine();
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("Error receiving message from the server");
//            closeEverything(socket, bufferedReader, bufferedWriter);
//            return null;
//        }
//
//    }
//
//    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
//        try {
//            if (bufferedReader != null) {
//                bufferedReader.close();
//            }
//            if (bufferedWriter != null) {
//                bufferedWriter.close();
//            }
//            if (socket != null) {
//                socket.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
//----------------------------------------------------works------------------------------------------------------------------

//package com.projex.javafx_chat.client;
//
//import javafx.application.Platform;
//import javafx.scene.layout.VBox;
//
//import java.io.*;
//import java.net.Socket;
//
//public class Client {
//
//    private Socket socket;
//    private BufferedReader bufferedReader;
//    private BufferedWriter bufferedWriter;
//    private String username;
//
//    public Socket getSocket() {
//        return socket;
//    }
//
//    public BufferedReader getBufferedReader() {
//        return bufferedReader;
//    }
//
//    public BufferedWriter getBufferedWriter() {
//        return bufferedWriter;
//    }
//
//    public Client(Socket socket, String username) {
//        try {
//            this.socket = socket;
//            this.username = username;
//            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//            // REMOVE THIS LINE:
//            // sendMessageToServer(username);
//        } catch (IOException e) {
//            e.printStackTrace();
//            closeEverything(socket, bufferedReader, bufferedWriter);
//        }
//    }
//
//
//    public void sendMessageToServer(String messageToServer) {
//        try {
//            bufferedWriter.write(messageToServer);
//            bufferedWriter.newLine();
//            bufferedWriter.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//            closeEverything(socket, bufferedReader, bufferedWriter);
//        }
//    }
//
//    public void receiveMessageFromServer(VBox vBox) {
//        new Thread(() -> {
//            while (socket.isConnected()) {
//                try {
//                    String messageFromServer = bufferedReader.readLine();
//                    if (messageFromServer != null) {
//                        System.out.println("Received: " + messageFromServer);
//                        Platform.runLater(() -> Controller.addLabel(messageFromServer, vBox));
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    closeEverything(socket, bufferedReader, bufferedWriter);
//                    break;
//                }
//            }
//        }).start();
//    }
//
//
//    public String receiveMessage() {
//        try {
//            return bufferedReader.readLine();
//        } catch (IOException e) {
//            e.printStackTrace();
//            closeEverything(socket, bufferedReader, bufferedWriter);
//            return null;
//        }
//    }
//
//    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
//        try {
//            if (bufferedReader != null) {
//                bufferedReader.close();
//                System.out.println("BufferedReader closed");
//            }
//            if (bufferedWriter != null) {
//                bufferedWriter.close();
//                System.out.println("BufferedWriter closed");
//            }
//            if (socket != null) {
//                socket.close();
//                System.out.println("Socket closed");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//}


//----------------------------------------------------------------------------------------------------------------------

package com.projex.javafx_chat.client;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;
    private Consumer<String> messageCallback;
    private Runnable shutdownCallback;

    public void setShutdownCallback(Runnable callback){
        this.shutdownCallback = callback;
    }

    public Socket getSocket(){
        return this.socket;
    }

    public Client(String username) {
        this.username = username;
        try {
            socket = new Socket("localhost", 1234);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Send username to server
            out.println(username);

            // Start listening for messages
            new Thread(this::receiveMessages).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    private void receiveMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if (message.equals("SERVER_SHUTDOWN")) {
                    if (shutdownCallback != null) {
                        Platform.runLater(shutdownCallback);
                    }
                    break;
                } else if (message.startsWith("CHAT_HISTORY:")) {
                    String chatHistory = message.substring("CHAT_HISTORY:".length());
                    if (messageCallback != null) {
                        messageCallback.accept("--- Chat History ---\n" + chatHistory);
                    }
                } else if (messageCallback != null) {
                    messageCallback.accept(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setMessageCallback(Consumer<String> callback) {
        this.messageCallback = callback;
    }

    public void closeEverything(){
        try {
            if(in != null) in.close();
            if(out!=null) out.close();
            if(socket!=null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
