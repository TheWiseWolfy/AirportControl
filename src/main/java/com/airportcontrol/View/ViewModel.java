package com.airportcontrol.View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ViewModel extends Application {

    @Override
    public void start(Stage stage) throws IOException {

      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
       Scene scene = new Scene(fxmlLoader.load(), 1200, 500);
       scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("application.css")).toExternalForm());
       stage.setTitle("Airport Controller!");
       stage.setScene(scene);
       stage.setResizable(false);
       stage.show();
    }



    public static void main(String[] args) {
        Application.launch();
    }
}