package de.josmer.dwdcdc.app.entities;

import de.josmer.dwdcdc.library.interfaces.ISolRad;

public final class SolRad implements ISolRad {

	private int gkhMax;
	private int gkhMin;
	private int gkrMax;
	private int gkrMin;
	private int radiationDate;
	private String radiationType;
	private float radiationValue;

	public SolRad() {
		this.radiationValue = (float) 0.0;
	}

	@Override
	public int getGkhMax() {
		return gkhMax;
	}

	@Override
	public int getGkhMin() {
		return gkhMin;
	}

	@Override
	public int getGkrMax() {
		return gkrMax;
	}

	@Override
	public int getGkrMin() {
		return gkrMin;
	}

	@Override
	public int getRadiationDate() {
		return radiationDate;
	}

	@Override
	public String getRadiationType() {
		return radiationType;
	}

	@Override
	public float getRadiationValue() {
		return radiationValue;
	}

	@Override
	public void setGkhMax(int gkhMax) {
		this.gkhMax = gkhMax;
	}

	@Override
	public void setGkhMin(int gkhMin) {
		this.gkhMin = gkhMin;
	}

	@Override
	public void setGkrMax(int gkrMax) {
		this.gkrMax = gkrMax;
	}

	@Override
	public void setGkrMin(int gkrMin) {
		this.gkrMin = gkrMin;
	}

	@Override
	public void setRadiationDate(int radiationDate) {
		this.radiationDate = radiationDate;
	}

	@Override
	public void setRadiationType(String radiationType) {
		this.radiationType = radiationType;
	}

	@Override
	public void setRadiationValue(float radiationValue) {
		this.radiationValue = radiationValue;
	}
}
