package com.airportcontrol.View;

import com.airportcontrol.DatabaseConnection;
import com.airportcontrol.Other.ErrorHandler;
import com.airportcontrol.View.TableClasses.Airport;
import com.airportcontrol.View.TableClasses.Flight;
import com.airportcontrol.View.TableClasses.Plane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsertFlightController {

    private ObservableList<String> airports = FXCollections.observableArrayList();
    private Map<String,Airport> airportMap = new HashMap<>();

    private ObservableList<Integer> planes = FXCollections.observableArrayList();



    private Flight toBeEdited;

    @FXML
    ChoiceBox<String> choiceBoxDeparture;
    @FXML
    ChoiceBox<String>  choiceBoxArrival;
    @FXML
    ChoiceBox<Integer>  choiceBoxPlaneID;

    @FXML
    Button buttonInsert;

    public static void display( ){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader( InsertFlightController.class.getResource("InsertFlight.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Insert Flight");
            stage.setScene(scene);
            stage.showAndWait();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void display(Flight flight){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader( InsertFlightController.class.getResource("InsertFlight.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            InsertFlightController instance = fxmlLoader.getController();
            instance.setToBeEdited(flight);

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Edit Flight");
            stage.setScene(scene);
            stage.showAndWait();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void setToBeEdited(Flight toBeEdited) {
        this.toBeEdited = toBeEdited;

        choiceBoxDeparture.getSelectionModel().select(toBeEdited.getFlightDepLocName());
        choiceBoxArrival.getSelectionModel().select(toBeEdited.getFlightArrLocName());

        choiceBoxDeparture.setDisable(true);
        choiceBoxPlaneID.setDisable(true);

        buttonInsert.setText("Edit Flight");

        //Here lies a small bug that requires too much effort to fix
    }

    @FXML
    public void initialize() {
        List<Airport> localAirports;

        //Populate drop boxes
        try {
            //Recovering the airports from the database
            localAirports = DatabaseConnection.getInstance().getDatabaseAirports();
            for (Airport airport : localAirports){
                airportMap.put( airport.getAirportName(),airport );
            }
            //Saving the names of the airpots for a dropbox selection
            airports.addAll( airportMap.keySet() );

            //Asociating a function with the act of selecting something
            choiceBoxDeparture.setOnAction(this::onDepLocationSelect);
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }

        choiceBoxDeparture.setItems(airports);
        choiceBoxArrival.setItems(airports);
        choiceBoxPlaneID.setItems(planes);
    }

    //When a selection is made, we enquire what planes are available there
    private void onDepLocationSelect(ActionEvent actionEvent) {
        List<Plane> localPlanes;
        planes.clear();
        choiceBoxPlaneID.setDisable(true);

        String desiredDepLocationName = choiceBoxDeparture.getSelectionModel().getSelectedItem();

        //if the selection is not null
        if( desiredDepLocationName != null) {
            int desiredDepID = airportMap.get(desiredDepLocationName).getAirportID();

            //Recovering the planes from the database
            try {
                localPlanes = DatabaseConnection.getInstance().getAvailablePlanesFromLocation(desiredDepID);
                for (Plane plane : localPlanes) {
                    planes.add(plane.getPlaneID());
                }

               /* if(toBeEdited != null){
                    planes.add( toBeEdited.getPlaneID() );
                }*/

                if (!planes.isEmpty()) {
                    choiceBoxPlaneID.setDisable(false);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void buttonAction(ActionEvent e) {
        DatabaseConnection con = DatabaseConnection.getInstance();

        try {
            String desiredDepLocationName = choiceBoxDeparture.getSelectionModel().getSelectedItem();
            int desiredDepID = airportMap.get(desiredDepLocationName).getAirportID();

            String desiredArrLocationName = choiceBoxArrival.getSelectionModel().getSelectedItem();
            int desiredArrID = airportMap.get(desiredArrLocationName).getAirportID();

            if( toBeEdited == null) {
                int desiredPlaneID = choiceBoxPlaneID.getSelectionModel().getSelectedItem();

                con.addFlight(desiredDepID, desiredArrID, desiredPlaneID);
            }else {
                con.editDatabaseFlight( toBeEdited.getFlightID(), desiredDepID,desiredArrID);
            }

            Stage stage = (Stage) choiceBoxPlaneID.getScene().getWindow();
            stage.close();
        }
        catch (RuntimeException ex){
            if( ex.getClass() ==  NullPointerException.class){
                ErrorHandler.SimpleError( "You cannot leave the dropbox blank.",  "Imput Error");
            }
            else { ex.printStackTrace(); }
        }
        catch (SQLException ex){
            if( ex.getErrorCode() == 2290){
                ErrorHandler.SimpleError( "A flight cannot go to the same place it started.","Stupid Error");
            }
            else { ex.printStackTrace(); }
        }

    }


}
