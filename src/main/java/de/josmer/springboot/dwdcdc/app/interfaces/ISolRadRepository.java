package de.josmer.springboot.dwdcdc.app.interfaces;

import de.josmer.springboot.dwdcdc.app.entities.SolRad;

import java.util.List;

public interface ISolRadRepository extends ISave<SolRad> {
    double[] findGlobal(int startDate, int endDate, double lon, double lat);

    List<SolRad> find(int startDate, int endDate, String radiationType, double lon, double lat);


    long getNumberOfRadiations();
}
