package de.josmer.springboot.dwdcdc.app.interfaces;

import de.josmer.springboot.dwdcdc.app.entities.SolRad;

import java.util.LinkedList;

public interface ISolRadRepository extends ISave<SolRad> {

    boolean isInTable(int date, String radiationType);

    double[] findGlobal(int startDate, int endDate, double lon, double lat);

    LinkedList<SolRad> find(int startDate, int endDate, String radiationType, double lon, double lat);

    long getNumberOfRadiations();
}
