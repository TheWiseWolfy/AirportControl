package com.airportcontrol.View;

import com.airportcontrol.DatabaseConnection;
import com.airportcontrol.Other.ErrorHandler;
import com.airportcontrol.View.TableClasses.Airport;
import com.airportcontrol.View.TableClasses.Plane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

    private ObservableList<String> airplanes = FXCollections.observableArrayList();
    private Map<String,Airport> airportMap = new HashMap<String, Airport>();

    private ObservableList<Integer> planes = FXCollections.observableArrayList();

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
            stage.setTitle("Insert Flight");
            stage.setScene(scene);
            stage.showAndWait();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
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
            airplanes.addAll( airportMap.keySet() );

            //Asociating a function with the act of selecting something
            choiceBoxDeparture.setOnAction(this::onDepLocationSelect);
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }

        choiceBoxDeparture.setItems(airplanes);
        choiceBoxArrival.setItems(airplanes);
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

                if (!planes.isEmpty()) {
                    choiceBoxPlaneID.setDisable(false);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void insertFlight(ActionEvent e) {
        DatabaseConnection con = DatabaseConnection.getInstance();

        String desiredDepLocationName = choiceBoxDeparture.getSelectionModel().getSelectedItem();
        int desiredDepID = airportMap.get(desiredDepLocationName).getAirportID();

        String desiredArrLocationName = choiceBoxArrival.getSelectionModel().getSelectedItem();
        int desiredArrID = airportMap.get(desiredArrLocationName).getAirportID();

        int desiredPlaneID = choiceBoxPlaneID.getSelectionModel().getSelectedItem();

        try {
            con.addFlight(desiredDepID,desiredArrID, desiredPlaneID );
        }
        catch (SQLException ex){
            if( ex.getErrorCode() == 2290){
                ErrorHandler.SimpleError( "A flight cannot go to the same place is started.","Stupid Error");
            }
            else { ex.printStackTrace(); }
        }
        finally {
            choiceBoxDeparture.getSelectionModel().clearSelection();
            choiceBoxArrival.getSelectionModel().clearSelection();
            choiceBoxPlaneID.getSelectionModel().clearSelection();
        }
    }


}
