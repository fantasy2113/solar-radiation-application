package de.josmer.dwdcdc.app.interfaces;

import de.josmer.dwdcdc.app.entities.SolRad;
import de.josmer.dwdcdc.library.enums.SolRadTypes;
import de.josmer.dwdcdc.library.interfaces.IBasicSolRad;

import java.util.LinkedList;

public interface ISolRadRepository extends IBasicSolRad {

    LinkedList<SolRad> find(int startDate, int endDate, SolRadTypes solRadTypes, double lon, double lat);

    double[] findGlobal(int startDate, int endDate, double lon, double lat);

    int getNumberOfRadiations();
}
