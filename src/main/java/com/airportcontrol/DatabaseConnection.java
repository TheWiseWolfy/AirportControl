package com.airportcontrol;

import com.airportcontrol.View.TableClasses.Airport;
import com.airportcontrol.View.TableClasses.Flight;
import com.airportcontrol.View.TableClasses.Plane;
import com.airportcontrol.View.TableClasses.Reservation;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {

    Connection con;

    //This is the instance of the singleton
    private static DatabaseConnection Instance = null;

    //This is how you use the database
    public static DatabaseConnection getInstance()
    {
        if (Instance == null)
            Instance = new DatabaseConnection();

        return Instance;
    }

    //A private contructor for the singleton
    private DatabaseConnection() {
        //step1 load the driver class
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@bd-dc.cs.tuiasi.ro:1539:orcl", "bd096", "bd096");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //Close connection
    public void closeConnection() {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ///_____________AIRPORT BUSINESS_____________

    // Add entry into table
    public void addAirport(String airportName ) throws SQLException {
        Statement stmt= null;

        // the mysql insert statement
        String query = " insert into AIRPORTS (AIRPORT_ID, AIRPORT_NAME)"
                + " values (seq_airport.nextval, ?)";

        // Properly insert into sql the airport
        PreparedStatement preparedStmt = con.prepareStatement(query);
        preparedStmt.setString (1, airportName);
        preparedStmt.execute();
    }

    //Recover all airports from the database
    public List<Airport> getDatabaseAirports() throws SQLException {
        List<Airport> airportsList = new ArrayList();

        String query = "select * from AIRPORTS";

        Statement preparedStmt = con.createStatement();
        ResultSet rs = preparedStmt.executeQuery(query);

        while (rs.next()){
            int id = rs.getInt("AIRPORT_ID");
            String name = rs.getString("AIRPORT_NAME");

            airportsList.add(new Airport(id,name ));
        }

        return airportsList;
    }

   public void deleteDatabaseAirport( int id) throws SQLException {
        Statement stmt= null;

        // the mysql insert statement
        String query = "DELETE FROM AIRPORTS WHERE AIRPORT_ID = ?";

        // Properly insert into sql the airport
        PreparedStatement preparedStmt = con.prepareStatement(query);
        preparedStmt.setInt (1, id);
        preparedStmt.execute();

    }

    public void editDatabaseAirport( int id, String newName) throws SQLException {

        // the mysql insert statement
        String query = "UPDATE AIRPORTS SET AIRPORT_NAME = ? WHERE AIRPORT_ID = ? ";

        // Properly insert into sql the airport
        PreparedStatement preparedStmt = con.prepareStatement(query);
        preparedStmt.setString (1, newName);
        preparedStmt.setInt (2, id);
        preparedStmt.execute();
    }


    //_____________PLANE BUSINESS_____________

    //Recover all planes from the database
    public List<Plane> getDatabasePlanes() throws SQLException {
        List<Plane> planesList = new ArrayList();

        String query ="SELECT PLANE_ID,PLANE_MODEL,BUSINESS_CAPACITY,ECONOMY_CAPACITY,LOCATION_ID,AIRPORT_NAME  \n" +
                    "FROM PLANES p, AIRPORTS a \n" +
                    "WHERE p.LOCATION_ID = a.AIRPORT_ID\n";

        Statement preparedStmt = con.createStatement();
        ResultSet rs = preparedStmt.executeQuery(query);

        while (rs.next()){
            int id = rs.getInt("PLANE_ID");
            String model = rs.getString("PLANE_MODEL");
            int businessCapacity = rs.getInt("BUSINESS_CAPACITY");
            int economyCapacity = rs.getInt("ECONOMY_CAPACITY" );
            int locationId = rs.getInt("LOCATION_ID");
            String locationName = rs.getString("AIRPORT_NAME");

            planesList.add(new Plane(id,model ,businessCapacity,economyCapacity ,locationId,locationName));
        }
        return planesList;
    }

    //Add plane in the database
    public void addPlane(String desiredModel, int desiredBusinessCapacity,int desiredEconomyCapacity, int desiredLocationId) throws SQLException {
        // the mysql insert statement
        String query = "insert into PLANES ( PLANE_ID, PLANE_MODEL, BUSINESS_CAPACITY,ECONOMY_CAPACITY, LOCATION_ID)\n" +
                        "VALUES (seq_plane.nextval, ? ,? ,?, ?)";

        // Properly insert into sql the airport
        PreparedStatement preparedStmt = con.prepareStatement(query);
        preparedStmt.setString (1, desiredModel);
        preparedStmt.setInt(2,desiredBusinessCapacity);
        preparedStmt.setInt(3,desiredEconomyCapacity);
        preparedStmt.setInt(4,desiredLocationId);

        preparedStmt.execute();
    }

    public void deleteDatabasePlane(int planeId) throws SQLException {
        String query = "DELETE FROM PLANES WHERE PLANE_ID = ?";

        // Properly insert into sql the airport
        PreparedStatement preparedStmt = con.prepareStatement(query);
        preparedStmt.setInt (1, planeId);
        preparedStmt.execute();
    }

    //Here we only return planes that are not used in other flights at also at that location.
    public List<Plane> getAvailablePlanesFromLocation(int locID) throws SQLException {
        List<Plane> planesList = new ArrayList();

        String query = "SELECT PLANE_ID,PLANE_MODEL,BUSINESS_CAPACITY,ECONOMY_CAPACITY,LOCATION_ID,AIRPORT_NAME\n" +
                "FROM PLANES, AIRPORTS a\n" +
                "WHERE LOCATION_ID = ? AND PLANE_ID NOT IN (SELECT PLANE_ID FROM FLIGHTS) AND LOCATION_ID = a.AIRPORT_ID";

        PreparedStatement preparedStmt = con.prepareStatement(query);
        preparedStmt.setInt (1, locID);

        ResultSet rs = preparedStmt.executeQuery();

        while (rs.next()){
            int id = rs.getInt("PLANE_ID");
            String model = rs.getString("PLANE_MODEL");
            int businessCapacity = rs.getInt("BUSINESS_CAPACITY");
            int economyCapacity = rs.getInt("ECONOMY_CAPACITY" );
            int locationId = rs.getInt("LOCATION_ID");
            String locationName = rs.getString("AIRPORT_NAME");

            planesList.add(new Plane(id,model ,businessCapacity,economyCapacity ,locationId,locationName));
        }
        return planesList;
    }


    //_____________FLIGHT BUSINESS_____________
    public List<Flight> getDatabaseFlights() throws SQLException {
        List<Flight> flightList = new ArrayList();

        String query = "SELECT FLIGHT_ID, FLIGHT_DEPARTURE_LOC, FLIGHT_ARRIVAL_LOC, PLANE_ID, a.AIRPORT_NAME AS DEP_NAME, b.AIRPORT_NAME as ARR_NAME\n" +
                "FROM FLIGHTS, AIRPORTS a, AIRPORTS b\n" +
                "WHERE FLIGHT_DEPARTURE_LOC = a.AIRPORT_ID AND FLIGHT_ARRIVAL_LOC = b.AIRPORT_ID";

        Statement preparedStmt = con.createStatement();
        ResultSet rs = preparedStmt.executeQuery(query);

        while (rs.next()){
            int id = rs.getInt("FLIGHT_ID");
            int depID = rs.getInt("FLIGHT_DEPARTURE_LOC");
            int arrID = rs.getInt("FLIGHT_ARRIVAL_LOC");
            int planeID = rs.getInt("PLANE_ID");
            String depName = rs.getString( "DEP_NAME");
            String arrName = rs.getString("ARR_NAME" );

            flightList.add(new Flight(id,depID,arrID,planeID,depName ,arrName));
        }
        return flightList;
    }

    //Add plane in the database
    public void addFlight(int desiredDepLocID, int desiredArrLocID, int desiredPlaneID) throws SQLException {
        // the mysql insert statement
        String query = "insert into FLIGHTS ( FLIGHT_ID, FLIGHT_DEPARTURE_LOC, FLIGHT_ARRIVAL_LOC, PLANE_ID)\n" +
                        "VALUES (seq_flights.nextval, ?, ? , ?  )";

        // Properly insert into sql the airport
        PreparedStatement preparedStmt = con.prepareStatement(query);
        preparedStmt.setInt (1, desiredDepLocID);
        preparedStmt.setInt(2, desiredArrLocID);
        preparedStmt.setInt(3, desiredPlaneID);

        preparedStmt.execute();
    }

    public void deleteDatabaseFlight(int flightID) throws SQLException {
        String query = "DELETE FROM FLIGHTS WHERE FLIGHT_ID = ?";

        // Properly insert into sql the airport
        PreparedStatement preparedStmt = con.prepareStatement(query);
        preparedStmt.setInt (1, flightID);
        preparedStmt.execute();
    }

    //_____________RESERVATION BUSINESS_____________

    public List<Reservation> getReservationsForFlight(int flightID) throws SQLException {
        List<Reservation> reservationList = new ArrayList();

        String query ="SELECT * FROM RESERVATIONS \n" +
                        "WHERE FLIGHT_ID = ?\n";

        PreparedStatement preparedStmt = con.prepareStatement(query);
        preparedStmt.setInt (1, flightID);

        ResultSet rs = preparedStmt.executeQuery();

        while (rs.next()){
            int id = rs.getInt("RESERVATION_ID");
            int businessSeats = rs.getInt("NUMBER_OF_ECONOMY_SEATS");
            int economySeats = rs.getInt("NUMBER_OF_BUSINESS_SEATS" );
            String holderName = rs.getString("HOLDER_NAME");
            int databaseFlightID = rs.getInt("FLIGHT_ID");

            reservationList.add(new Reservation(id ,businessSeats,economySeats ,holderName,databaseFlightID));
        }
        return reservationList;
    }

    //Add plane in the database
    public void addReservation(int numberOfEconomySeats, int numberOfBusinessSeats, String holderName, int flightID) throws SQLException {
        // the mysql insert statement
        String query = "insert into RESERVATIONS ( RESERVATION_ID, NUMBER_OF_ECONOMY_SEATS, NUMBER_OF_BUSINESS_SEATS ,HOLDER_NAME, FLIGHT_ID)\n" +
                        "VALUES (seq_reservations.nextval, ?,?,?, ? )";

        // Properly insert into sql the airport
        PreparedStatement preparedStmt = con.prepareStatement(query);

        preparedStmt.setInt (1, numberOfEconomySeats);
        preparedStmt.setInt(2, numberOfBusinessSeats);
        preparedStmt.setString(3, holderName);
        preparedStmt.setInt(4, flightID);

        preparedStmt.execute();
    }



}