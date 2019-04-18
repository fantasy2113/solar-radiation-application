package de.josmer.application.entities;

public class ExportExtracted extends Export {
    private String year;
    private String month;
    private double as;
    private double ys;
    private double eGlobHor;
    private double eGlobGen;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getAs() {
        return as;
    }

    public void setAs(double as) {
        this.as = as;
    }

    public double getYs() {
        return ys;
    }

    public void setYs(double ys) {
        this.ys = ys;
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
