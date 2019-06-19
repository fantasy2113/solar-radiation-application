package de.josmer.dwdcdc.app.base.interfaces;

import de.josmer.dwdcdc.app.base.entities.SolIrr;

import java.util.LinkedList;

public interface ISolIrrRepository {
    LinkedList<SolIrr> getIrradiation(double[] eGlobHorMonthly, double lon, double lat, int ae, int ye, int year);
}
