package de.josmer.app.library.interfaces;

import de.josmer.app.entities.Radiation;

import java.util.List;

public interface IRadiationRepository {
    double[] findGlobal(final IGaussKrueger gaussKrueger, int startDate, int endDate, double lon, double lat);

    List<Radiation> find(IGaussKrueger gaussKrueger, int startDate, int endDate, String type, double lon, double lat);

    void save(List<Radiation> radiations);

    long count();
}