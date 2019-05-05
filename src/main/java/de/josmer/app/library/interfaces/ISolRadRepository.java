package de.josmer.app.library.interfaces;

import de.josmer.app.model.entities.SolRad;

import java.util.List;

public interface ISolRadRepository {

    double[] findGlobal(final IGaussKrueger gaussKrueger, int startDate, int endDate, double lon, double lat);

    List<SolRad> find(IGaussKrueger gaussKrueger, int startDate, int endDate, String type, double lon, double lat);

    void save(List<SolRad> radiations);

    long count();
}