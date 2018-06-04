package com.example.saini.maproute;

public class Customer {

    String cus_name;
    String id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String type = "customer";

    public Customer(String cus_name, long phone) {
        this.cus_name = cus_name;

        this.phone = phone;
    }
    public Customer() {}

    public String getCus_name() {
        return cus_name;
    }

    public void setCus_name(String cus_name) {
        this.cus_name = cus_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    long phone;
}
