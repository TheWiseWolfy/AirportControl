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

    private Airport toBeEdited;

    @FXML
    Button buttonInsert;

    @FXML
    TextField textFieldInsert;

    @FXML
    public static void display() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(InsertAirportController.class.getResource("InsertAirport.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Insert Airport");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void display(Airport airport) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(InsertAirportController.class.getResource("InsertAirport.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            InsertAirportController instance = fxmlLoader.getController();
            instance.setToBeEdited(airport);

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Edit Airport");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void setToBeEdited(Airport toBeEdited) {
        this.toBeEdited = toBeEdited;

        textFieldInsert.setText(toBeEdited.getAirportName());
        buttonInsert.setText("Edit Airport");
    }

    @FXML
    public void buttonAction(ActionEvent e) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        try {
            String desiredName = textFieldInsert.getText();
            if (desiredName.isBlank()) {
                throw new RuntimeException("blank");
            }

            if (toBeEdited == null) {
                databaseConnection.addAirport(desiredName);
            } else {
                databaseConnection.editDatabaseAirport(toBeEdited.getAirportID(), desiredName);
            }
            Stage stage = (Stage) buttonInsert.getScene().getWindow();
            stage.close();
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1) {
                ErrorHandler.SimpleError("There is already an airport named like that.", "Imput Error");
            }else if(ex.getErrorCode() == 12899){
                ErrorHandler.SimpleError("The name you are trying to introduce is too long.", "Imput Error");
            } else { ex.printStackTrace(); }
        } catch (Exception ex) {
            if (Objects.equals(ex.getMessage(), "blank")) {
                ErrorHandler.SimpleError("You cannot leave the model field blank.", "Imput Error");
            } else {
                ex.printStackTrace();
            }
        }
    }
}