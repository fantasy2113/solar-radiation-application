package de.jos.dwdcdc.app.interfaces;

import de.jos.dwdcdc.app.entities.SolIrr;

import java.util.LinkedList;

public interface ISolIrrRepository {

    LinkedList<SolIrr> getIrradiation(double[] eGlobHorMonthly, double lon, double lat, int ae, int ye, int year);
}
