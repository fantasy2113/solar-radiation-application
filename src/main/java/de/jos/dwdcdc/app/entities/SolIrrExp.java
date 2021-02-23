package de.jos.dwdcdc.app.entities;

public final class SolIrrExp extends Export {

  private String ae;
  private double eGlobGen;
  private double eGlobHor;
  private String ye;

  public String getAe() {
    return ae;
  }

  public void setAe(String ae) {
    this.ae = ae;
  }

  public double geteGlobGen() {
    return eGlobGen;
  }

  public void seteGlobGen(double eGlobGen) {
    this.eGlobGen = eGlobGen;
  }

  public double geteGlobHor() {
    return eGlobHor;
  }

  public void seteGlobHor(double eGlobHor) {
    this.eGlobHor = eGlobHor;
  }

  public String getYe() {
    return ye;
  }

  public void setYe(String ye) {
    this.ye = ye;
  }
}
