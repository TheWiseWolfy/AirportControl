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
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsertPlaneController {

    private ObservableList<String> locations = FXCollections.observableArrayList();
    private Map<String,Airport> airportMap = new HashMap<String, Airport>();

    @FXML
    Button buttonInsert;

    @FXML
    TextField textFieldInsertModel;
    @FXML
    TextField textFieldInsertBusiness;
    @FXML
    TextField textFieldInsertEconomy;

    @FXML
    ChoiceBox<String> choiceBoxLocation;

    @FXML
    public static void display( ){
        try {

            FXMLLoader fxmlLoader = new FXMLLoader( InsertPlaneController.class.getResource("InsertPlane.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage stage = new Stage();
            stage.setTitle("Insert Plane");
            stage.setScene(scene);
            stage.showAndWait();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        List<Airport> airports;

        //Populate drop boxes
        try {
            airports = DatabaseConnection.getInstance().getDatabaseAirports();
            for (Airport airport : airports){
                airportMap.put( airport.getAirportName(),airport );
            }
            locations.addAll( airportMap.keySet() );
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }

        choiceBoxLocation.setItems(locations);
    }

    public void insertPlane(ActionEvent e) {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

        //Recover values from the UI and local dictionary
        String desiredModel = textFieldInsertModel.getText();
        Integer desiredBusinessCapacity = Integer.parseInt( textFieldInsertBusiness.getText() );
        Integer desiredEconomyCapacity = Integer.parseInt( textFieldInsertEconomy.getText() );

        String desiredLocationName = choiceBoxLocation.getSelectionModel().getSelectedItem();
        Integer desiredLocationId = airportMap.get(desiredLocationName).getAirportID();

        try {
            databaseConnection.addPlane(desiredModel,desiredBusinessCapacity,desiredEconomyCapacity,desiredLocationId );
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        finally {
            textFieldInsertModel.clear();
            textFieldInsertBusiness.clear();
            choiceBoxLocation.getSelectionModel().clearSelection();
        }
    }


}
