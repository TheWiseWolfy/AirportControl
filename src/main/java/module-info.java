module com.example.airportcontrol {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.airportcontrol to javafx.fxml;
    exports com.airportcontrol;
    exports com.airportcontrol.View;
    opens com.airportcontrol.View to javafx.fxml;
    exports com.airportcontrol.View.TableClasses;
    opens com.airportcontrol.View.TableClasses to javafx.fxml;
}