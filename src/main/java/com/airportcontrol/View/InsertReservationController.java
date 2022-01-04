package com.airportcontrol.View;

import com.airportcontrol.DatabaseConnection;
import com.airportcontrol.View.TableClasses.Airport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsertReservationController {

    private int flightID;

    @FXML
    Button buttonInsert;

    @FXML
    TextField textFieldInsertHolder;
    @FXML
    TextField textFieldInsertBusiness;
    @FXML
    TextField textFieldInsertEconomy;

    @FXML
    public static void display(int flightID ){
        try {

            FXMLLoader fxmlLoader = new FXMLLoader( InsertReservationController.class.getResource("InsertReservation.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            InsertReservationController instance = (InsertReservationController)fxmlLoader.getController();
            instance.setFlightID(flightID);

            Stage stage = new Stage();
            stage.setTitle("Create reservation for flight " + flightID);
            stage.setScene(scene);
            stage.showAndWait();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
    }

    public void insertPlane(ActionEvent e) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        //Recover values from the UI and local dictionary
        String holderName = textFieldInsertHolder.getText();
        Integer desiredBusinessCapacity = Integer.parseInt( textFieldInsertBusiness.getText() );
        Integer desiredEconomyCapacity = Integer.parseInt( textFieldInsertEconomy.getText() );

        try {
            databaseConnection.addReservation(desiredEconomyCapacity,desiredBusinessCapacity,holderName, flightID );
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        finally {
            textFieldInsertHolder.clear();
            textFieldInsertEconomy.clear();
            textFieldInsertBusiness.clear();
        }
    }

    public void setFlightID(int flightID) {
        this.flightID = flightID;
    }

}
