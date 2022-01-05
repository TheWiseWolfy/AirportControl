package com.airportcontrol.View.TableClasses;

public class Flight {

    private final int FlightID;
    private final int FlightDepLocID;
    private final int FlightArrLocID;
    private final int PlaneID;

    private final String FlightDepLocName;
    private final String FlightArrLocName;



    private final int BusinessSeatsLeft;
    private final int EconomySeatsLeft;

    public Flight(int flightID, int flightDepLocID, int flightArrLocID, int planeID, String flightDepLocName, String flightArrLocName, int businessSeatsLeft, int economySeatsLeft){
      FlightID = flightID;
      FlightDepLocID = flightDepLocID;
      FlightArrLocID = flightArrLocID;
      PlaneID = planeID;
      FlightDepLocName = flightDepLocName;
      FlightArrLocName = flightArrLocName;
      BusinessSeatsLeft = businessSeatsLeft;
      EconomySeatsLeft = economySeatsLeft;
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

    public int getBusinessSeatsLeft() {
        return BusinessSeatsLeft;
    }

    public int getEconomySeatsLeft() {
        return EconomySeatsLeft;
    }
}
