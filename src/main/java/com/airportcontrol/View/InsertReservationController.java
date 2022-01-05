package com.airportcontrol.View;

import com.airportcontrol.DatabaseConnection;
import com.airportcontrol.Other.ErrorHandler;
import com.airportcontrol.View.TableClasses.Airport;
import com.airportcontrol.View.TableClasses.Reservation;
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
import java.util.Objects;

public class InsertReservationController {

    private int flightID;
    private Reservation toBeEdited;

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

            InsertReservationController instance = fxmlLoader.getController();
            instance.setFlightID(flightID);

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Create reservation for flight " + flightID);
            stage.setScene(scene);
            stage.showAndWait();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public static void display(Reservation reservation){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader( InsertReservationController.class.getResource("InsertReservation.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            InsertReservationController instance = fxmlLoader.getController();
            instance.setFlightID(reservation.getFlightID());
            instance.setToBeEdited(reservation);

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Create reservation for flight " + reservation.getFlightID());
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

    public void setToBeEdited(Reservation toBeEdited) {
        this.toBeEdited = toBeEdited;

        textFieldInsertHolder.setText( toBeEdited.getHolderName() );

        textFieldInsertBusiness.setText( String.valueOf( toBeEdited.getBusinessSeats() ) );
        textFieldInsertBusiness.setDisable(true);
        textFieldInsertEconomy.setText( String.valueOf( toBeEdited.getEconomySeats() ) );
        textFieldInsertEconomy.setDisable(true);

        buttonInsert.setText("Edit Reservation");

    }

    public void insertPlane(ActionEvent e) {
        DatabaseConnection com = DatabaseConnection.getInstance();


        try {
            //Recover values from the UI and local dictionary
            String holderName = textFieldInsertHolder.getText();
            if (holderName.isEmpty()){
                throw new RuntimeException("blank");
            }

            int desiredBusinessCapacity = Integer.parseInt( textFieldInsertBusiness.getText() );
            int desiredEconomyCapacity = Integer.parseInt( textFieldInsertEconomy.getText() );

            if(toBeEdited == null) {
                com.addReservation(desiredEconomyCapacity, desiredBusinessCapacity, holderName, flightID);
            }else {
                com.editDatabaseReservation( toBeEdited.getReservationID() , desiredEconomyCapacity, desiredBusinessCapacity, holderName, flightID);
            }

            Stage stage = (Stage) textFieldInsertHolder.getScene().getWindow();
            stage.close();
        }
        catch (SQLException ex){
            if( ex.getErrorCode() == 2290){
                ErrorHandler.SimpleError( "You cannot reserve more seats than they are available or negative seats","Imput Error");
            }else if(ex.getErrorCode() == 12899){
                ErrorHandler.SimpleError("The model you are trying to introduce is too long.", "Imput Error");
            }else { ex.printStackTrace(); }
        }
        catch (Exception ex) {
            if (ex.getClass() == NumberFormatException.class) {
                ErrorHandler.SimpleError("You cannot add letters to the numeric fields or leave them black", "Imput Error");
            }
            else if(Objects.equals(ex.getMessage(), "blank")){
                ErrorHandler.SimpleError( "You cannot leave the name field blank.",  "Imput Error");
            }else { ex.printStackTrace(); }
        }
    }

    public void setFlightID(int flightID) {
        this.flightID = flightID;
    }

}
