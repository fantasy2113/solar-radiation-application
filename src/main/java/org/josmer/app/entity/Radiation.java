package org.josmer.app.entity;

public class Radiation {
    private int date;
    private int yMin;
    private int yMax;
    private int xMin;
    private int xMax;
    private float value;
    private String typ;

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

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }
}
