package com.airportcontrol.View;

import com.airportcontrol.DatabaseConnection;
import javafx.event.ActionEvent;
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

public class InsertAirportController {

    @FXML
    Button buttonInsert;

    @FXML
    TextField textFieldInsert;

    public static void display( ){
        try {
            Parent root = FXMLLoader.load(InsertAirportController.class.getResource("InsertAirport.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Insert Airport");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }


    @FXML
    void insertAirport(ActionEvent e) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        String desiredName = textFieldInsert.getText();

        try {
            databaseConnection.addAirport(desiredName);
        }
        catch (SQLException ex){

            if(  ex.getErrorCode() == 1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Insert Error");
                alert.setHeaderText(null);
                alert.setContentText(desiredName + " is already in the database !");

                alert.showAndWait();
            }
        }
        finally {
            textFieldInsert.clear();
        }
    }


}
