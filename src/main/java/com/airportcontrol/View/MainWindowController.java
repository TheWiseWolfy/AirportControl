package com.airportcontrol.View;

import com.airportcontrol.DatabaseConnection;
import com.airportcontrol.View.TableClasses.Airport;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class MainWindowController {
    ObservableList<Airport> zboruri = FXCollections.observableArrayList();

    @FXML
    TableView<Airport> airportTableView;

    @FXML
    Button Button1;

    //Fuctia asta e rulata doar la initializare
    @FXML
    public void initialize() {
        initializeTable();
        updateTableValues();
    }


    @FXML
    void insertAirportWindow(ActionEvent e) {
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
                e.printStackTrace();
            }
        }
        updateTableValues();
    }

    @FXML
    void editSelectedAirport(){
        Airport selectedAirport = airportTableView.getSelectionModel().getSelectedItem();

/*        if( selectedAirport != null) {
            try {
               // DatabaseConnection.getInstance().editDatabaseAirport(selectedAirport.getAirportID(), new Airport( id,name));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        updateTableValues();*/
    }


    private void initializeTable(){
        TableColumn<Airport,String> AvionIDTableCollumn = new TableColumn<>("Airport Name");
        AvionIDTableCollumn.setCellValueFactory(new PropertyValueFactory<>("AirportName"));

        airportTableView.getColumns().add(AvionIDTableCollumn);
        airportTableView.setItems(zboruri);
    }


    private void updateTableValues(){
        zboruri.clear();

        try {
            zboruri.addAll(DatabaseConnection.getInstance().getDatabaseAirports());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }





}