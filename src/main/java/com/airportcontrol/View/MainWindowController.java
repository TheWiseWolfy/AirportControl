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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class MainWindowController {

    ObservableList<Airport> airports = FXCollections.observableArrayList();
    ObservableList<Plane> planes = FXCollections.observableArrayList();
    ObservableList<Flight> flights = FXCollections.observableArrayList();

    @FXML
    TableView<Airport> airportTableView;
    @FXML
    TableView<Plane> planeTableView;
    @FXML
    TableView<Flight> flightTableView;

    //Fuctia asta e rulata doar la initializare

    @FXML
    public void initialize() {
        initializeTable();
        updateTableValues();
    }


    //_____________AIRPORT TAB_____________
    @FXML
    void insertAirport(ActionEvent e) {
        InsertAirportController.display();
        updateTableValues();
    }

    @FXML
    void deleteSelectedAirport(){
        Airport selectedAirport = airportTableView.getSelectionModel().getSelectedItem();

        if( selectedAirport != null) {
            try {
                DatabaseConnection.getInstance().deleteDatabaseAirport(selectedAirport.getAirportID());
            } catch (SQLException e) {
                if(e.getErrorCode() == 2292){
                    ErrorHandler.SimpleError( "There are still planes and flights using this location","Depedency Error");
                }
                else{ e.printStackTrace(); }
            }
        }
        updateTableValues();
    }

    @FXML
    void editSelectedAirport(){
        Airport selectedAirport = airportTableView.getSelectionModel().getSelectedItem();

        if( selectedAirport != null) {
            EditAirportController.display(selectedAirport);

            updateTableValues();
        }
    }

    //_____________PLANES TAB_____________

    @FXML
    void insertPlaneWindow(ActionEvent e) {
        InsertPlaneController.display();
        updateTableValues();
    }

    @FXML
   void deleteSelectedPlane(){
        Plane selectedPlane = planeTableView.getSelectionModel().getSelectedItem();

        if( selectedPlane != null) {
            try {
                DatabaseConnection.getInstance().deleteDatabasePlane(selectedPlane.getPlaneID());
            } catch (SQLException e) {
                if(e.getErrorCode() == 2292){
                    ErrorHandler.SimpleError( "There are still flights  using this plane","Depedency Error");
                }else { e.printStackTrace(); }
            }
        }
        updateTableValues();
    }

    @FXML
    void editSelectedPlane(){
    }

    //_____________FLIGHT TAB_____________

    @FXML
    void insertFlight(ActionEvent e) {
        InsertFlightController.display();
        updateTableValues();
    }

    @FXML
    void deleteSelectedFlight(){
        Flight selectedFlight = flightTableView.getSelectionModel().getSelectedItem();

        if( selectedFlight != null) {
            try {
                DatabaseConnection.getInstance().deleteDatabaseFlight(selectedFlight.getFlightID() );
            } catch (SQLException e) {
                if(e.getErrorCode() == 2292){
                    ErrorHandler.SimpleError( "There are still flights  using this plane","Depedency Error");
                }else { e.printStackTrace(); }
            }
        }
        updateTableValues();
    }

    //_____________GENERAL FUCTIONS_____________
    private void initializeTable(){


        //Initialize the Flights Tab
        TableColumn<Flight,Integer> FlightIdCollum = new TableColumn<>("ID");
        FlightIdCollum.setCellValueFactory(new PropertyValueFactory<>("FlightID"));

        TableColumn<Flight,String> FlightDepNameCollum = new TableColumn<>("Departure Location");
        FlightDepNameCollum.setCellValueFactory(new PropertyValueFactory<>("FlightDepLocName"));

        TableColumn<Flight,String> FlightArrNameCollum = new TableColumn<>("Arrival Location");
        FlightArrNameCollum.setCellValueFactory(new PropertyValueFactory<>("FlightArrLocName"));

        TableColumn<Flight,Integer> FlightPlaneIdCollum = new TableColumn<>("Plane ID");
        FlightPlaneIdCollum.setCellValueFactory(new PropertyValueFactory<>("PlaneID"));

        flightTableView.getColumns().add(FlightIdCollum);
        flightTableView.getColumns().add(FlightDepNameCollum);
        flightTableView.getColumns().add(FlightArrNameCollum);
        flightTableView.getColumns().add(FlightPlaneIdCollum);
        flightTableView.setItems(flights);

        //Initialize the Planes Tab
        TableColumn<Plane,Integer> PlaneIdCollum = new TableColumn<>("ID");
        PlaneIdCollum.setCellValueFactory(new PropertyValueFactory<>("PlaneID"));

        TableColumn<Plane,String> PlaneModelCollum = new TableColumn<>("Model");
        PlaneModelCollum.setCellValueFactory(new PropertyValueFactory<>("PlaneModel"));

        TableColumn<Plane,Integer> PlaneBusinessCapacityCollum = new TableColumn<>("Business Capacity");
        PlaneBusinessCapacityCollum.setCellValueFactory(new PropertyValueFactory<>("BusinessCapacity"));

        TableColumn<Plane,Integer> PlaneEconomyCapacityCollum = new TableColumn<>("Economy Capacity");
        PlaneEconomyCapacityCollum.setCellValueFactory(new PropertyValueFactory<>("EconomyCapacity"));

        TableColumn<Plane,Integer> PlaneLocationCollum = new TableColumn<>("Location");
        PlaneLocationCollum.setCellValueFactory(new PropertyValueFactory<>("PlaneLocationName"));

        planeTableView.getColumns().add(PlaneIdCollum);
        planeTableView.getColumns().add(PlaneModelCollum);
        planeTableView.getColumns().add(PlaneBusinessCapacityCollum);
        planeTableView.getColumns().add(PlaneEconomyCapacityCollum);
        planeTableView.getColumns().add(PlaneLocationCollum);
        planeTableView.setItems(planes);

        //Initialize Airport table
        TableColumn<Airport,String> AirportNameTable = new TableColumn<>("Airport Name");
        AirportNameTable.setCellValueFactory(new PropertyValueFactory<>("AirportName"));

        airportTableView.getColumns().add(AirportNameTable);
        airportTableView.setItems(airports);
    }


    private void updateTableValues(){
        airports.clear();
        planes.clear();
        flights.clear();

        try {
            airports.addAll(DatabaseConnection.getInstance().getDatabaseAirports());
            planes.addAll(DatabaseConnection.getInstance().getDatabasePlanes());
            flights.addAll(  DatabaseConnection.getInstance().getDatabaseFlights() );
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}