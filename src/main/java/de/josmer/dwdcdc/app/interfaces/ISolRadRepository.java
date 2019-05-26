package de.josmer.dwdcdc.app.interfaces;

import de.josmer.dwdcdc.utils.entities.SolRad;
import de.josmer.dwdcdc.utils.enums.SolRadTypes;
import de.josmer.dwdcdc.utils.interfaces.ISaveSolRad;

import java.util.LinkedList;

public interface ISolRadRepository extends ISaveSolRad {

    double[] findGlobal(int startDate, int endDate, double lon, double lat);

    LinkedList<SolRad> find(int startDate, int endDate, SolRadTypes solRadTypes, double lon, double lat);

    long getNumberOfRadiations();
}
