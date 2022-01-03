package com.airportcontrol.Other;

import javafx.scene.control.Alert;

public class ErrorHandler {

    public static void DuplicateError(String desiredName){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Insert Error");
        alert.setHeaderText(null);
        alert.setContentText(desiredName + " is already in the database !");

        alert.showAndWait();
    }

    public static void EmptyFieldError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Empty Field Error");
        alert.setHeaderText(null);
        alert.setContentText("One of the required fields is empty");

        alert.showAndWait();
    }

    public static void SimpleError(String errorText, String errorTitle){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(errorTitle);
        alert.setHeaderText(null);
        alert.setContentText(errorText);

        alert.showAndWait();
    }

}
