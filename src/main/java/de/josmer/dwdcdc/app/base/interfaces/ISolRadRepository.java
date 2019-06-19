package de.josmer.dwdcdc.app.base.interfaces;

import de.josmer.dwdcdc.app.base.entities.SolRad;
import de.josmer.dwdcdc.library.enums.SolRadTypes;
import de.josmer.dwdcdc.library.interfaces.IBasicSolRad;

import java.util.LinkedList;

public interface ISolRadRepository extends IBasicSolRad {

    double[] findGlobal(int startDate, int endDate, double lon, double lat);

    LinkedList<SolRad> find(int startDate, int endDate, SolRadTypes solRadTypes, double lon, double lat);

    int getNumberOfRadiations();
}
