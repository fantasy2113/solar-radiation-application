package org.josmer.entities;

public class Radiation {
    private long id;
    private int date;
    private int yMin;
    private int yMax;
    private int xMin;
    private int xMax;
    private double radiation;
    private String typ;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getyMin() {
        return yMin;
    }

    public void setyMin(int yMin) {
        this.yMin = yMin;
    }

    public int getyMax() {
        return yMax;
    }

    public void setyMax(int yMax) {
        this.yMax = yMax;
    }

    public int getxMin() {
        return xMin;
    }

    public void setxMin(int xMin) {
        this.xMin = xMin;
    }

    public int getxMax() {
        return xMax;
    }

    public void setxMax(int xMax) {
        this.xMax = xMax;
    }

    public double getRadiation() {
        return radiation;
    }

    public void setRadiation(double radiation) {
        this.radiation = radiation;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }
}
