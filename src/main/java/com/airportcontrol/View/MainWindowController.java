package com.airportcontrol.View;

import com.airportcontrol.DatabaseConnection;
import com.airportcontrol.Other.ErrorHandler;
import com.airportcontrol.View.TableClasses.Airport;

import com.airportcontrol.View.TableClasses.Flight;
import com.airportcontrol.View.TableClasses.Plane;
import com.airportcontrol.View.TableClasses.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.*;

public class MainWindowController {

     private ObservableList<Airport> airports = FXCollections.observableArrayList();
     private ObservableList<Plane> planes = FXCollections.observableArrayList();
     private ObservableList<Flight> flights = FXCollections.observableArrayList();
    private ObservableList<Reservation> reservations = FXCollections.observableArrayList();

    private List<Button> buttonList;

    private Map<String,Integer> flightsMap = new HashMap<>();
    private ObservableList<String> flightsName = FXCollections.observableArrayList();

    @FXML
    TableView<Airport> airportTableView;
    @FXML
    TableView<Plane> planeTableView;
    @FXML
    TableView<Flight> flightTableView;
    @FXML
    TableView<Reservation> reservationsTableView;
    @FXML
    ChoiceBox<String> choiceBoxFlight;
    @FXML
    TextArea detailsBox;
    @FXML
    Button AirportButton1;
    @FXML
    Button AirportButton2;
    @FXML
    Button AirportButton3;
    @FXML
    Button PlanesButton1;
    @FXML
    Button PlanesButton2;
    @FXML
    Button PlanesButton3;
    @FXML
    Button FlightButton1;
    @FXML
    Button FlightButton2;
    @FXML
    Button FlightButton3;
    @FXML
    Button ReservationButton1;
    @FXML
    Button ReservationButton2;
    @FXML
    Button ReservationButton3;

    //Fuctia asta e rulata doar la initializare
    @FXML
    public void initialize() {
        initializeTable();
        updateTableValues();

        //This has to be at the end
        initializeDropbox();

        buttonList = Arrays.asList(AirportButton1,AirportButton2,AirportButton3,PlanesButton1,PlanesButton2,PlanesButton3,
                                    FlightButton1,FlightButton2,FlightButton3,ReservationButton1,ReservationButton2,ReservationButton3);
    }

    //_____________AIRPORT TAB_____________
    @FXML
    void insertAirport(ActionEvent e) {
        setButtonsDisable(true);
        InsertAirportController.display();
        updateTableValues();
        setButtonsDisable(false);
    }

    @FXML
    void deleteSelectedAirport(){
        Airport selectedAirport = airportTableView.getSelectionModel().getSelectedItem();

        if( selectedAirport != null) {
            try {
                DatabaseConnection.getInstance().deleteDatabaseAirport(selectedAirport.getAirportID());
                updateTableValues();
            } catch (SQLException e) {
                if(e.getErrorCode() == 2292){
                    ErrorHandler.SimpleError( "There are still planes and flights using this location","Depedency Error");
                }
                else{ e.printStackTrace(); }
            }
        }else{
            ErrorHandler.SimpleError("You need to select something first.","No selection !");
        }
    }

    @FXML
    void editSelectedAirport(){
        Airport selectedAirport = airportTableView.getSelectionModel().getSelectedItem();

        if( selectedAirport != null) {
            setButtonsDisable(true);
            InsertAirportController.display(selectedAirport);
            updateTableValues();
            setButtonsDisable(false);
        }else{
            ErrorHandler.SimpleError("You need to select something first.","No selection !");
        }
    }

    //_____________PLANES TAB_____________

    @FXML
    void insertPlaneWindow(ActionEvent e) {
        setButtonsDisable(true);
        InsertPlaneController.display();
        updateTableValues();
        setButtonsDisable(false);
    }

    @FXML
   void deleteSelectedPlane(){
        Plane selectedPlane = planeTableView.getSelectionModel().getSelectedItem();

        if( selectedPlane != null) {
            try {
                DatabaseConnection.getInstance().deleteDatabasePlane(selectedPlane.getPlaneID());
                updateTableValues();
            } catch (SQLException e) {
                if(e.getErrorCode() == 2292){
                    ErrorHandler.SimpleError( "There are still flights  using this plane","Depedency Error");
                }else { e.printStackTrace(); }
            }
        }else{
            ErrorHandler.SimpleError("You need to select something first.","No selection !");
        }
    }

    @FXML
    void editSelectedPlane(){
        Plane selectedPlane = planeTableView.getSelectionModel().getSelectedItem();
        if( selectedPlane != null) {
            setButtonsDisable(true);
            InsertPlaneController.display(selectedPlane);
            updateTableValues();
            setButtonsDisable(false);
        }else{
            ErrorHandler.SimpleError("You need to select something first.","No selection !");
        }
    }

    //_____________FLIGHT TAB_____________

    @FXML
    void insertFlight(ActionEvent e) {
        setButtonsDisable(true);
        InsertFlightController.display();
        updateTableValues();
        updateDropbox();
        setButtonsDisable(false);
    }

    @FXML
    void deleteSelectedFlight(){
        Flight selectedFlight = flightTableView.getSelectionModel().getSelectedItem();

        if( selectedFlight != null) {
            try {
                DatabaseConnection.getInstance().deleteDatabaseFlight( selectedFlight.getFlightID() );
            } catch (SQLException e) {
                if(e.getErrorCode() == 2292){
                    ErrorHandler.SimpleError( "There are still flights  using this plane.","Depedency Error");
                }else { e.printStackTrace(); }
            }
        }else{
            ErrorHandler.SimpleError("You need to select something first.","No selection !");
        }
        updateTableValues();
        updateDropbox();
    }

    @FXML
    private void editSelectedFlight(ActionEvent actionEvent){
        Flight selectedFlight = flightTableView.getSelectionModel().getSelectedItem();
        if( selectedFlight != null) {
            setButtonsDisable(true);
            InsertFlightController.display(selectedFlight);
            updateTableValues();
            setButtonsDisable(false);
        }else{
            ErrorHandler.SimpleError("You need to select something first.","No selection !");
        }
    }

    @FXML
    private void onFlightSelect(ActionEvent actionEvent) {
        updateTableValues();
    }

    //_____________RESERVATIONS TAB_____________

    @FXML
    void insertReservation(ActionEvent e) {
        if( choiceBoxFlight.getSelectionModel().getSelectedItem() != null)  {
            setButtonsDisable(true);
            String flightName = choiceBoxFlight.getSelectionModel().getSelectedItem();
            int flightID = flightsMap.get(flightName);
            InsertReservationController.display( flightID );
            updateTableValues();
            setButtonsDisable(false);
        }else{
            ErrorHandler.SimpleError("You need to select a flight before making a reservation.","No selection !");
        }
    }

    @FXML
    void deleteReservation( ActionEvent e){
        Reservation selectedReservation = reservationsTableView.getSelectionModel().getSelectedItem();

        if( selectedReservation != null) {
            try {
                DatabaseConnection.getInstance().deleteDatabaseReservation(selectedReservation);
                updateTableValues();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }else{
            ErrorHandler.SimpleError("You need to select something first.","No selection !");
        }
    }

    @FXML
    void editSelectedReservation(ActionEvent e){
        Reservation selectedReservation = reservationsTableView.getSelectionModel().getSelectedItem();
        if( selectedReservation != null) {
            setButtonsDisable(true);
            InsertReservationController.display(selectedReservation);
            updateTableValues();
            setButtonsDisable(false);
        }else{
            ErrorHandler.SimpleError("You need to select something first.","No selection !");
        }
    }

    //_____________GENERAL FUCTIONS_____________

    private void initializeTable(){

        //Initialize the Rezervations tab _________________________________________
        TableColumn<Reservation,Integer> ReservationIDCollum = new TableColumn<>("ID");
        ReservationIDCollum.setCellValueFactory(new PropertyValueFactory<>("ReservationID"));

        TableColumn<Reservation,Integer> ReservationEconomySeatsCollum = new TableColumn<>("Economy Seats Reserved");
        ReservationEconomySeatsCollum.setCellValueFactory(new PropertyValueFactory<>("EconomySeats"));

        TableColumn<Reservation,Integer> ReservationBusinessSeatsCollum = new TableColumn<>("Business Seats Reserved");
        ReservationBusinessSeatsCollum.setCellValueFactory(new PropertyValueFactory<>("BusinessSeats"));

        TableColumn<Reservation,String> ReservationHolderNameCollum = new TableColumn<>("Reservation Holder");
        ReservationHolderNameCollum.setCellValueFactory(new PropertyValueFactory<>("HolderName"));

        reservationsTableView.getColumns().add(ReservationIDCollum);
        reservationsTableView.getColumns().add(ReservationEconomySeatsCollum);
        reservationsTableView.getColumns().add(ReservationBusinessSeatsCollum);
        reservationsTableView.getColumns().add(ReservationHolderNameCollum);
        reservationsTableView.setItems(reservations);

        //Initialize the Flights Tab _________________________________________
        TableColumn<Flight,Integer> FlightIdCollum = new TableColumn<>("ID");
        FlightIdCollum.setCellValueFactory(new PropertyValueFactory<>("FlightID"));

        TableColumn<Flight,String> FlightDepNameCollum = new TableColumn<>("Departure Location");
        FlightDepNameCollum.setCellValueFactory(new PropertyValueFactory<>("FlightDepLocName"));

        TableColumn<Flight,String> FlightArrNameCollum = new TableColumn<>("Arrival Location");
        FlightArrNameCollum.setCellValueFactory(new PropertyValueFactory<>("FlightArrLocName"));

        TableColumn<Flight,Integer> FlightPlaneIdCollum = new TableColumn<>("Plane ID");
        FlightPlaneIdCollum.setCellValueFactory(new PropertyValueFactory<>("PlaneID"));

        TableColumn<Flight,Integer> FlightBusinessSeatsCollum = new TableColumn<>("Business Seats Left");
        FlightBusinessSeatsCollum.setCellValueFactory(new PropertyValueFactory<>("BusinessSeatsLeft"));

        TableColumn<Flight,Integer> FlightEconomySeatsCollum = new TableColumn<>("Economy Seats Left");
        FlightEconomySeatsCollum.setCellValueFactory(new PropertyValueFactory<>("EconomySeatsLeft"));

        flightTableView.getColumns().add(FlightIdCollum);
        flightTableView.getColumns().add(FlightDepNameCollum);
        flightTableView.getColumns().add(FlightArrNameCollum);
        flightTableView.getColumns().add(FlightPlaneIdCollum);
        flightTableView.getColumns().add(FlightBusinessSeatsCollum);
        flightTableView.getColumns().add(FlightEconomySeatsCollum);

        flightTableView.setItems(flights);

        //Initialize the Planes Tab _________________________________________
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

        //Initialize Airport table _________________________________________
        TableColumn<Airport,String> AirportNameColumn = new TableColumn<>("Airport Name");
        AirportNameColumn.setCellValueFactory(new PropertyValueFactory<>("AirportName"));

        TableColumn<Airport,String> AirportIDColumn = new TableColumn<>("Airport ID");
        AirportIDColumn.setCellValueFactory(new PropertyValueFactory<>("AirportID"));

        airportTableView.getColumns().add(AirportIDColumn);
        airportTableView.getColumns().add(AirportNameColumn);
        airportTableView.setItems(airports);
    }

    private void initializeDropbox(){
        choiceBoxFlight.setOnAction(this::onFlightSelect);
        choiceBoxFlight.setItems(flightsName);
        updateDropbox();
    }

    public void updateDropbox(){
        flightsMap.clear();
        flightsName.clear();

        for (Flight flight : flights){
            String key = flight.getFlightID() +": "+ flight.getFlightDepLocName() +" to "+ flight.getFlightArrLocName();
            flightsMap.put(key ,flight.getFlightID() );
        }
        flightsName.addAll( flightsMap.keySet() );
    }

    private void setButtonsDisable(boolean disable){
        for(Button button: buttonList){
            button.setDisable(disable);
        }
    }

    public void updateDetailsBox(int flightID) {
        try {
            Flight flight = DatabaseConnection.getInstance().getDatabaseFlightByID(flightID);

            String finalText = "";
            finalText += "This flight will depart from: " + flight.getFlightDepLocName();
            finalText += "and arrive at: " + flight.getFlightArrLocName() + ".\n";
            finalText += "There are " + flight.getEconomySeatsLeft() + " economy class seats left";
            finalText += " and " + flight.getBusinessSeatsLeft() + " business class seats left.\n";
            detailsBox.setText(finalText);
        } catch (Exception e) {
            if(!Objects.equals(e.getMessage(), "bad")){
                e.printStackTrace();
            }
        }
    }

    private void updateTableValues(){
        airports.clear();
        planes.clear();
        flights.clear();
        reservations.clear();

        try {
            airports.addAll(DatabaseConnection.getInstance().getDatabaseAirports());
            planes.addAll(DatabaseConnection.getInstance().getDatabasePlanes());
            flights.addAll(  DatabaseConnection.getInstance().getDatabaseFlights() );

           if( choiceBoxFlight.getSelectionModel().getSelectedItem() != null)  {
               String flightName = choiceBoxFlight.getSelectionModel().getSelectedItem();
               int flightID = flightsMap.get(flightName);
               reservations.addAll( DatabaseConnection.getInstance().getReservationsForFlight( flightID ) );
               updateDetailsBox( flightID );
           }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}