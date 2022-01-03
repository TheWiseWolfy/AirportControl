package com.airportcontrol.View;

import com.airportcontrol.DatabaseConnection;
import com.airportcontrol.Other.ErrorHandler;
import com.airportcontrol.View.TableClasses.Airport;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class InsertAirportController {

    @FXML
    Button buttonInsert;

    @FXML
    TextField textFieldInsert;

    @FXML
    public static void display( ){
        try {

            FXMLLoader fxmlLoader = new FXMLLoader( InsertAirportController.class.getResource("InsertAirport.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage stage = new Stage();
            stage.setTitle("Insert Airport");
            stage.setScene(scene);
            stage.showAndWait();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void insertAirport(ActionEvent e) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        String desiredName = textFieldInsert.getText();

        if( desiredName.isBlank() ){
            ErrorHandler.EmptyFieldError();
            return;
        }

        try {
            databaseConnection.addAirport(desiredName);
        }
        catch (SQLException ex){
            if(  ex.getErrorCode() == 1) {
                ErrorHandler.DuplicateError(desiredName);
            }
        }
        finally {
            textFieldInsert.clear();
        }
    }


}
