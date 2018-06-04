package com.example.saini.maproute;

public class Driver {
    String driver_name;
    String vehicle_type;
    String license;
    String vehicle_no;
    String type = "driver";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    String id;
    long loads,phone_no;
    double latitude=0.0,longitude=0.0;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Driver(String driver_name, String vehicle_type, String license, String vehicle_no, long loads, long phone_no) {
        this.driver_name = driver_name;
        this.vehicle_type = vehicle_type;
        this.license = license;
        this.vehicle_no = vehicle_no;
        this.loads = loads;
        this.phone_no = phone_no;
    }

    public Driver() {

    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public long getLoads() {
        return loads;
    }

    public void setLoads(long loads) {
        this.loads = loads;
    }

    public long getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(long phone_no) {
        this.phone_no = phone_no;
    }

    public String getDriver_name() {

        return driver_name;
    }

}
