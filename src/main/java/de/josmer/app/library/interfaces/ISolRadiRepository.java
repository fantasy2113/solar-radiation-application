package de.josmer.app.library.interfaces;

import de.josmer.app.model.entities.SolRadi;

import java.util.List;

public interface ISolRadiRepository {

    double[] findGlobal(final IGaussKrueger gaussKrueger, int startDate, int endDate, double lon, double lat);

    List<SolRadi> find(IGaussKrueger gaussKrueger, int startDate, int endDate, String type, double lon, double lat);

    void save(List<SolRadi> radiations);

    long count();
}
