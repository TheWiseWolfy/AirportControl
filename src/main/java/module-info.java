module com.example.airportcontrol {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.airportcontrol to javafx.fxml;
    exports com.airportcontrol;
}