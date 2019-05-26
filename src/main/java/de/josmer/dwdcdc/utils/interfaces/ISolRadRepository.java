package de.josmer.dwdcdc.utils.interfaces;

import de.josmer.dwdcdc.app.interfaces.ISave;
import de.josmer.dwdcdc.utils.entities.SolRad;

import java.util.LinkedList;

public interface ISolRadRepository extends ISave<SolRad> {

    boolean isInTable(int date, String radiationType);

    double[] findGlobal(int startDate, int endDate, double lon, double lat);

    LinkedList<SolRad> find(int startDate, int endDate, String radiationType, double lon, double lat);

    long getNumberOfRadiations();
}
