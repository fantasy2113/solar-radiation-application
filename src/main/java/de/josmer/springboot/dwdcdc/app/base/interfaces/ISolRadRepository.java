package de.josmer.springboot.dwdcdc.app.base.interfaces;

import java.util.List;

public interface ISolRadRepository {
    double[] findGlobal(int startDate, int endDate, double lon, double lat);

    List<ISolRad> find(int startDate, int endDate, String radiationType, double lon, double lat);

    void save(List<ISolRad> radiations);

    long getNumberOfRadiations();
}
