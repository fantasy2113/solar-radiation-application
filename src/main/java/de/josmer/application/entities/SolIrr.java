package de.josmer.application.entities;

public class SolIrr {

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

    public double geteGlobHor() {
        return eGlobHor;
    }

    public void seteGlobHor(double eGlobHor) {
        this.eGlobHor = eGlobHor;
    }

    public double geteGlobGen() {
        return eGlobGen;
    }

    public void seteGlobGen(double eGlobGen) {
        this.eGlobGen = eGlobGen;
    }
}
