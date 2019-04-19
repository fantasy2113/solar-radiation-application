package de.josmer.app.entities;

public class ExportCalc extends AExport {
    private double ae;
    private double ye;
    private double eGlobHor;
    private double eGlobGen;

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

    public void setEGlobHor(double eGlobHor) {
        this.eGlobHor = eGlobHor;
    }

    public double geteGlobGen() {
        return eGlobGen;
    }

    public void setEGlobGen(double eGlobGen) {
        this.eGlobGen = eGlobGen;
    }
}
