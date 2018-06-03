package com.example.saini.maproute;

public class VehicleInformation {
    long id;
    String vehicle_Type;

    public long getId() {
        return id;
    }

    public String getVehicle_Type() {
        return vehicle_Type;
    }

    public String getVehicle_No() {
        return vehicle_No;
    }

    public long getLoads() {
        return loads;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    String vehicle_No;
    long loads;
    double latitude;
    double longitude;

    public VehicleInformation() {

    }

}
