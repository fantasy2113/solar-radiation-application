package de.josmer.springboot.entities;

import de.josmer.springboot.base.interfaces.ISolRad;

public final class SolRad implements ISolRad {
    private int radiationDate;
    private int gkhMin;
    private int gkhMax;
    private int gkrMin;
    private int gkrMax;
    private float radiationValue;
    private String radiationType;

    public SolRad() {
        this.radiationValue = (float) 0.0;
    }

    @Override
    public int getRadiationDate() {
        return radiationDate;
    }

    @Override
    public void setRadiationDate(int radiationDate) {
        this.radiationDate = radiationDate;
    }

    @Override
    public int getGkhMin() {
        return gkhMin;
    }

    @Override
    public void setGkhMin(int gkhMin) {
        this.gkhMin = gkhMin;
    }

    @Override
    public int getGkhMax() {
        return gkhMax;
    }

    @Override
    public void setGkhMax(int gkhMax) {
        this.gkhMax = gkhMax;
    }

    @Override
    public int getGkrMin() {
        return gkrMin;
    }

    @Override
    public void setGkrMin(int gkrMin) {
        this.gkrMin = gkrMin;
    }

    @Override
    public int getGkrMax() {
        return gkrMax;
    }

    @Override
    public void setGkrMax(int gkrMax) {
        this.gkrMax = gkrMax;
    }

    @Override
    public float getRadiationValue() {
        return radiationValue;
    }

    @Override
    public void setRadiationValue(float radiationValue) {
        this.radiationValue = radiationValue;
    }

    @Override
    public String getRadiationType() {
        return radiationType;
    }

    @Override
    public void setRadiationType(String radiationType) {
        this.radiationType = radiationType;
    }
}
