package com.example.saini.maproute;

public class Tracker {
    String id;
    String from;
    String to;

    public Tracker() {

    }

    public Tracker(String id,String from,String to) {
        this.id = id;
        this.from = from;
        this.to = to;

    }

    public String getId() {
        return id;
    }


    public String getFrom() {
        return from;
    }



    public String getTo() {
        return to;
    }


}
