package com.proj.messengerserver.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("server.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/com/proj/messengerserver/server/server.fxml"));
        System.out.println("FXML location: " + root);
        stage.setTitle("Server");
        stage.setScene(new Scene(root, 478,396));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}