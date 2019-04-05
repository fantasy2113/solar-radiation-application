package org.josmer.app.entity;

public class Export {

    private String date;
    private double lat;
    private double lon;
    private String typ;
    private float value;


    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
