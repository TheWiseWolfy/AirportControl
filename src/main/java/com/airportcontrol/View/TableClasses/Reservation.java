package com.airportcontrol.View.TableClasses;

public class Reservation {

    private int ReservationID;

    private int EconomySeats;
    private int BusinessSeats;
    private String HolderName;
    private int FlightID;

    public Reservation(int reservationID, int economySeats, int businessSeats, String holderName, int flightID) {
        ReservationID = reservationID;
        EconomySeats = economySeats;
        BusinessSeats = businessSeats;
        HolderName = holderName;
        FlightID = flightID;
    }

    public int getReservationID() {
        return ReservationID;
    }

    public int getEconomySeats() {
        return EconomySeats;
    }

    public int getBusinessSeats() {
        return BusinessSeats;
    }

    public String getHolderName() {
        return HolderName;
    }

    public int getFlightID() {
        return FlightID;
    }
}
