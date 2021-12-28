package com.airportcontrol;

import com.airportcontrol.View.TableClasses.Airport;
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

    ///AIRPORT BUSINESS

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
        ResultSet rs = preparedStmt.executeQuery("select * from AIRPORTS\n");

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



}