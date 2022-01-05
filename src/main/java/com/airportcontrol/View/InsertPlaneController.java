package com.airportcontrol.View;

import com.airportcontrol.DatabaseConnection;
import com.airportcontrol.Other.ErrorHandler;
import com.airportcontrol.View.TableClasses.Airport;
import com.airportcontrol.View.TableClasses.Plane;
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
import java.util.Objects;

public class InsertPlaneController {

    private Plane toBeEdited = null;
    private ObservableList<String> locations = FXCollections.observableArrayList();
    private Map<String,Airport> airportMap = new HashMap<>();

    @FXML
    TextField textFieldInsertModel;
    @FXML
    TextField textFieldInsertBusiness;
    @FXML
    TextField textFieldInsertEconomy;
    @FXML
    Button buttonInsert;
    @FXML
    ChoiceBox<String> choiceBoxLocation;

    @FXML
    public static void display( ){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader( InsertPlaneController.class.getResource("InsertPlane.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Insert Plane");
            stage.setScene(scene);
            stage.showAndWait();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    //If we call this fuction, we will use the window in edit mode
    @FXML
    public static void display(Plane plane){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader( InsertPlaneController.class.getResource("InsertPlane.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            InsertPlaneController instance = fxmlLoader.getController();
            instance.setToBeEdited(plane);

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Edit Plane");
            stage.setScene(scene);
            stage.showAndWait();
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void setToBeEdited(Plane toBeEdited) {
        this.toBeEdited = toBeEdited;

        textFieldInsertModel.setText(toBeEdited.getPlaneModel());
        textFieldInsertBusiness.setText( String.valueOf(toBeEdited.getBusinessCapacity() ) );
        textFieldInsertEconomy.setText( String.valueOf(toBeEdited.getEconomyCapacity() ) );

        choiceBoxLocation.getSelectionModel().select(toBeEdited.getPlaneLocationName());
        buttonInsert.setText("Edit Plane");

        textFieldInsertBusiness.setDisable(true);
        textFieldInsertEconomy.setDisable(true);

    }

    //Here we populate the airport dropdown menu.
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

    @FXML
    public void buttonAction(ActionEvent e) {
        DatabaseConnection con = DatabaseConnection.getInstance();

        try {
            String desiredModel = textFieldInsertModel.getText();
            if (desiredModel.isBlank()) {
                throw new RuntimeException("blank");
            }
            int desiredBusinessCapacity = Integer.parseInt(textFieldInsertBusiness.getText());
            int desiredEconomyCapacity = Integer.parseInt(textFieldInsertEconomy.getText());

            String desiredLocationName = choiceBoxLocation.getSelectionModel().getSelectedItem();
            int desiredLocationId = airportMap.get(desiredLocationName).getAirportID();

            if(toBeEdited == null) {
                con.addPlane(desiredModel, desiredBusinessCapacity, desiredEconomyCapacity, desiredLocationId);
            }else {
                con.editPlane( toBeEdited.getPlaneID(),desiredModel, desiredBusinessCapacity, desiredEconomyCapacity, desiredLocationId);
            }

            Stage stage = (Stage) choiceBoxLocation.getScene().getWindow();
            stage.close();
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 2290) {
                ErrorHandler.SimpleError("You cannot have a negative or too small number of seats", "Imput Error");
            }else if(ex.getErrorCode() == 12899){
                ErrorHandler.SimpleError("The model you are trying to introduce is too long.", "Imput Error");
            } else {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            if (ex.getClass() == NumberFormatException.class) {
                ErrorHandler.SimpleError("You cannot add letters to the numeric fields or leave them black", "Imput Error");
            } else if (ex.getClass() == NullPointerException.class) {
                ErrorHandler.SimpleError("You cannot leave the dropbox blank.", "Imput Error");
            } else if (Objects.equals(ex.getMessage(), "blank")) {
                ErrorHandler.SimpleError("You cannot leave the model field blank.", "Imput Error");
            } else if (Objects.equals(ex.getMessage(), "magic")) {
                ErrorHandler.SimpleError("You cannot move a plane that will take off from there.", "Imput Error");
            } else {
                ex.printStackTrace();
            }
        }
    }
}
