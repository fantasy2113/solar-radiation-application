package de.josmer.dwdcdc.app.interfaces;

import java.util.LinkedList;

import de.josmer.dwdcdc.app.entities.SolIrr;

public interface ISolIrrRepository {

	LinkedList<SolIrr> getIrradiation(double[] eGlobHorMonthly, double lon, double lat, int ae, int ye, int year);
}
