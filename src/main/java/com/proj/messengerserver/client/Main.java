package com.proj.messengerserver.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/proj/messengerserver/client/client.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader.load(getClass().getResource("client.fxml"))
        stage.setTitle("Client");
        stage.setScene(new Scene(root, 476,396));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}