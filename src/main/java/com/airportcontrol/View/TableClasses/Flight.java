package com.airportcontrol.View.TableClasses;

public class Flight {

    private int FlightID;
    private int FlightDepLocID;
    private int FlightArrLocID;
    private int PlaneID;

    private String FlightDepLocName;
    private String FlightArrLocName;

    public Flight(int flightID, int flightDepLocID, int flightArrLocID, int planeID, String flightDepLocName, String flightArrLocName){
       this.FlightID = flightID;
       this.FlightDepLocID = flightDepLocID;
       this.FlightArrLocID = flightArrLocID;
       this.PlaneID = planeID;

       this.FlightDepLocName = flightDepLocName;
       this.FlightArrLocName = flightArrLocName;
    }

    public int getFlightID() {
        return FlightID;
    }

    public int getFlightDepLocID() {
        return FlightDepLocID;
    }

    public int getFlightArrLocID() {
        return FlightArrLocID;
    }

    public int getPlaneID() {
        return PlaneID;
    }

    public String getFlightDepLocName() {
        return FlightDepLocName;
    }

    public String getFlightArrLocName() {
        return FlightArrLocName;
    }

}
