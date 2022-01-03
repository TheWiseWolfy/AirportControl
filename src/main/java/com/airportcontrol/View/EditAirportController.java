package com.airportcontrol.View;

import com.airportcontrol.DatabaseConnection;
import com.airportcontrol.View.TableClasses.Airport;
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

public class EditAirportController {

    public Airport toBeEdited;

    @FXML
    Button buttonInsert;

    @FXML
    TextField textFieldInsert;

    public static void display(Airport airport  ){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader( InsertAirportController.class.getResource("EditAirport.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            EditAirportController instance = (EditAirportController)fxmlLoader.getController();
            instance.toBeEdited = airport;
            instance.textFieldInsert.setText(airport.getAirportName());

            Stage stage = new Stage();
            stage.setTitle("Edit Airport");
            stage.setScene(scene);
            stage.showAndWait();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void editAirport(ActionEvent e) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        String desiredName = textFieldInsert.getText();

        try {
            databaseConnection.editDatabaseAirport( toBeEdited.getAirportID(),desiredName );
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        finally {
            Stage stage = (Stage) textFieldInsert.getScene().getWindow();
            stage.close();
        }
    }
}
