package com.airportcontrol.View;

import com.airportcontrol.DatabaseConnection;
import com.airportcontrol.Other.ErrorHandler;
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


        try {
            //Recover values from the UI and local dictionary
            String holderName = textFieldInsertHolder.getText();
            if (holderName.isEmpty()){
                throw new RuntimeException("blank");
            }

            int desiredBusinessCapacity = Integer.parseInt( textFieldInsertBusiness.getText() );
            int desiredEconomyCapacity = Integer.parseInt( textFieldInsertEconomy.getText() );

            databaseConnection.addReservation(desiredEconomyCapacity,desiredBusinessCapacity,holderName, flightID );

            Stage stage = (Stage) textFieldInsertHolder.getScene().getWindow();
            stage.close();
        }
        catch (SQLException ex){
            if( ex.getErrorCode() == 2290){
                ErrorHandler.SimpleError( "You cannot reserve more seats than they are available or negative seats","Imput Error");
            }else { ex.printStackTrace(); }
        }
        catch (Exception ex) {
            if (ex.getClass() == NumberFormatException.class) {
                ErrorHandler.SimpleError("You cannot add letters to the numeric fields or leave them black", "Imput Error");
            }
            else if( ex.getMessage() == "blank" ){
                ErrorHandler.SimpleError( "You cannot leave the name field blank.",  "Imput Error");
            }else { ex.printStackTrace(); }
        }
    }

    public void setFlightID(int flightID) {
        this.flightID = flightID;
    }

}
