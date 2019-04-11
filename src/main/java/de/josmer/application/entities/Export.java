package de.josmer.application.entities;

public class Export {

    private String datum;
    private String lat;
    private String lon;
    private String art;
    private double wert;
    private String einheit;
    private String dim;
    private String quelle;

    public double getWert() {
        return wert;
    }

    public void setWert(double wert) {
        this.wert = wert;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getEinheit() {
        return einheit;
    }

    public void setEinheit(String einheit) {
        this.einheit = einheit;
    }

    public String getQuelle() {
        return quelle;
    }

    public void setQuelle(String quelle) {
        this.quelle = quelle;
    }

    public String getDim() {
        return dim;
    }

    public void setDim(String dim) {
        this.dim = dim;
    }
}
