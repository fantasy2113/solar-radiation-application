package de.josmer.dwdcdc.app.entities;

public final class SolIrr {

	private double ae;
	private String calculatedDate;
	private double eGlobGen;
	private double eGlobHor;
	private double ye;

	public double getAe() {
		return ae;
	}

	public String getCalculatedDate() {
		return calculatedDate;
	}

	public double geteGlobGen() {
		return eGlobGen;
	}

	public double geteGlobHor() {
		return eGlobHor;
	}

	public double getYe() {
		return ye;
	}

	public void setAe(double ae) {
		this.ae = ae;
	}

	public void setCalculatedDate(String calculatedDate) {
		this.calculatedDate = calculatedDate;
	}

	public void seteGlobGen(double eGlobGen) {
		this.eGlobGen = eGlobGen;
	}

	public void seteGlobHor(double eGlobHor) {
		this.eGlobHor = eGlobHor;
	}

	public void setYe(double ye) {
		this.ye = ye;
	}
}
