package de.josmer.application.entities;

import de.josmer.application.library.abstractclasses.Export;

public class ExportRadiation extends Export {
    private String date;
    private String type;
    private double value;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
