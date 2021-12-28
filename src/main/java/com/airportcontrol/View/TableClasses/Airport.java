package com.airportcontrol.View.TableClasses;

public class Airport {

    private int AirportID;
    private String AirportName;

    public Airport( int id, String name){
        AirportID = id;
        AirportName = name;
    }

    public int getAirportID() {
        return AirportID;
    }

    public String getAirportName() {
        return AirportName;
    }
}
