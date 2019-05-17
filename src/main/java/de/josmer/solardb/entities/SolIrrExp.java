package de.josmer.solardb.entities;

public final class SolIrrExp extends Export {
    private String ae;
    private String ye;
    private double eGlobHor;
    private double eGlobGen;

    public String getAe() {
        return ae;
    }

    public void setAe(String ae) {
        this.ae = ae;
    }

    public String getYe() {
        return ye;
    }

    public void setYe(String ye) {
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
