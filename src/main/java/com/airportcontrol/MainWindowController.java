package com.airportcontrol;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainWindowController {
    ObservableList<Zbor> zboruri = FXCollections.observableArrayList();

    @FXML
    TableView<Zbor> zboruriListView;

    @FXML
    Button uniqueButton;
    @FXML
    Button uniqueButton2;


    @FXML
    void write(ActionEvent e){
        System.out.println( "Button out");

        TableColumn<Zbor,String> IDTableCollumn = new TableColumn<>("ID");
        TableColumn<Zbor,String> AvionIDTableCollumn = new TableColumn<>("Avion ID");
        TableColumn<Zbor,String> DestinatieTableCollumn = new TableColumn<>("Destinatie");

        IDTableCollumn.setCellValueFactory(new PropertyValueFactory<>("ZborID"));
        AvionIDTableCollumn.setCellValueFactory(new PropertyValueFactory<>("AvionID"));
        DestinatieTableCollumn.setCellValueFactory(new PropertyValueFactory<>("Destinatie"));

        zboruriListView.getColumns().addAll(IDTableCollumn,AvionIDTableCollumn,DestinatieTableCollumn);
         zboruriListView.setItems(getZbor());


    }

    @FXML
    void update(ActionEvent e) {

        zboruri.add(new Zbor("6", "2", "Vrajonia",22) );

        //zboruri.remove(2);
    }

    public ObservableList<Zbor> getZbor(){

        zboruri.add( new Zbor("1", "2", "Vinlanda",33));
        zboruri.add( new Zbor("2", "6", "Posaca",32));

        return zboruri;
    }



}