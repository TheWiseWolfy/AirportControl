package com.airportcontrol.View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewModel extends Application {

    @Override
    public void start(Stage stage) throws IOException {

      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
       Scene scene = new Scene(fxmlLoader.load(), 600, 500);
       stage.setTitle("Hello!");
       stage.setScene(scene);
       stage.show();
    }



    public static void main(String[] args) {
        Application.launch();
    }
}