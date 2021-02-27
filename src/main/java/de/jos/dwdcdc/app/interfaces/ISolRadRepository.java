package de.jos.dwdcdc.app.interfaces;

import de.jos.dwdcdc.app.entities.SolRad;
import de.jos.dwdcdc.library.enums.SolRadTypes;
import de.jos.dwdcdc.share.IBasicSolRad;

import java.util.LinkedList;

public interface ISolRadRepository extends IBasicSolRad {

  LinkedList<SolRad> find(int startDate, int endDate, SolRadTypes solRadTypes, double lon, double lat);

  double[] findGlobal(int startDate, int endDate, double lon, double lat);

  int getNumberOfRadiations();
}
