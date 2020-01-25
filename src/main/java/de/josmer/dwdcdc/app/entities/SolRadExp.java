package de.josmer.dwdcdc.app.entities;

public final class SolRadExp extends Export {

    private String type;
    private double value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
