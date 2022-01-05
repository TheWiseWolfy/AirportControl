package com.airportcontrol.View.TableClasses;

public class Plane {

    private final int PlaneID;
    private final String PlaneModel;
    private final int BusinessCapacity;
    private final int EconomyCapacity;
    private final int PlaneLocationId;
    private final String PlaneLocationName;


    public Plane(int planeID, String model, int businessCapacity, int economyCapacity, int planeLocationId, String planeLocationName){
        this.PlaneID = planeID;
        this.PlaneModel = model;
        this.BusinessCapacity = businessCapacity;
        this.EconomyCapacity = economyCapacity;
        this.PlaneLocationId = planeLocationId;
        this.PlaneLocationName = planeLocationName;
    }

    //GETTERS
    public int getPlaneID() {
        return PlaneID;
    }

    public String getPlaneLocationName() {
        return PlaneLocationName;
    }

    public String getPlaneModel() {
        return PlaneModel;
    }

    public int getBusinessCapacity() {
        return BusinessCapacity;
    }

    public int getEconomyCapacity() {
        return EconomyCapacity;
    }

    public int getPlaneLocationId() {
        return PlaneLocationId;
    }
}
