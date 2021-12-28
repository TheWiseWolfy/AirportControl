package com.airportcontrol.View.TableClasses;

public class Zbor {

    private String ZborID;
    private String AvionID;
    private String Destinatie;
    private int NrLocuri;

    public Zbor(){
        ZborID = "";
        AvionID = "";
        Destinatie = "";
        NrLocuri = 0;
    }

    public Zbor(String ZborID ,String AvionID,  String Destinatie, int NrLocuri){
        this.ZborID = ZborID;
        this.AvionID = AvionID;
        this.Destinatie = Destinatie;
        this.NrLocuri = NrLocuri;
    }

    public String getZborID() {
        return ZborID;
    }

    public String getAvionID() {
        return AvionID;
    }

    public String getDestinatie() {
        return Destinatie;
    }

    public int getNrLocuri() {
        return NrLocuri;
    }

}
