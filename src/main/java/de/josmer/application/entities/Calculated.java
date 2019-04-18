package de.josmer.application.entities;

public class Calculated {
    private String calculatedDate;
    private double ae;
    private double ye;
    private double eGlobHor;
    private double eGlobGen;

    public String getCalculatedDate() {
        return calculatedDate;
    }

    public void setCalculatedDate(String calculatedDate) {
        this.calculatedDate = calculatedDate;
    }

    public double getAe() {
        return ae;
    }

    public void setAe(double ae) {
        this.ae = ae;
    }

    public double getYe() {
        return ye;
    }

    public void setYe(double ye) {
        this.ye = ye;
    }

    public double getEGlobHor() {
        return eGlobHor;
    }

    public void setEGlobHor(double eGlobHor) {
        this.eGlobHor = eGlobHor;
    }

    public double getEGlobGen() {
        return eGlobGen;
    }

    public void setEGlobGen(double eGlobGen) {
        this.eGlobGen = eGlobGen;
    }
}
