package de.josmer.application.entities;

public class Radiation {

    private int radiationDate;
    private int gkhMin;
    private int gkhMax;
    private int gkrMin;
    private int gkrMax;
    private float radiationValue;
    private String radiationType;

    public Radiation() {
        this.radiationValue = (float) 0.0;
    }

    public int getRadiationDate() {
        return radiationDate;
    }

    public void setRadiationDate(int radiationDate) {
        this.radiationDate = radiationDate;
    }

    public int getGkhMin() {
        return gkhMin;
    }

    public void setGkhMin(int gkhMin) {
        this.gkhMin = gkhMin;
    }

    public int getGkhMax() {
        return gkhMax;
    }

    public void setGkhMax(int gkhMax) {
        this.gkhMax = gkhMax;
    }

    public int getGkrMin() {
        return gkrMin;
    }

    public void setGkrMin(int gkrMin) {
        this.gkrMin = gkrMin;
    }

    public int getGkrMax() {
        return gkrMax;
    }

    public void setGkrMax(int gkrMax) {
        this.gkrMax = gkrMax;
    }

    public float getRadiationValue() {
        return radiationValue;
    }

    public void setRadiationValue(float radiationValue) {
        this.radiationValue = radiationValue;
    }

    public String getRadiationType() {
        return radiationType;
    }

    public void setRadiationType(String radiationType) {
        this.radiationType = radiationType;
    }
}
