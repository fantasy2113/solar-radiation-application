package de.josmer.dwdcdc.app.entities;

public final class SolIrrExp extends Export {

	private String ae;
	private double eGlobGen;
	private double eGlobHor;
	private String ye;

	public String getAe() {
		return ae;
	}

	public double geteGlobGen() {
		return eGlobGen;
	}

	public double geteGlobHor() {
		return eGlobHor;
	}

	public String getYe() {
		return ye;
	}

	public void setAe(String ae) {
		this.ae = ae;
	}

	public void seteGlobGen(double eGlobGen) {
		this.eGlobGen = eGlobGen;
	}

	public void seteGlobHor(double eGlobHor) {
		this.eGlobHor = eGlobHor;
	}

	public void setYe(String ye) {
		this.ye = ye;
	}
}
