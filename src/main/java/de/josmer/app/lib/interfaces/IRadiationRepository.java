package de.josmer.app.lib.interfaces;

import de.josmer.app.entities.Radiation;

import java.util.List;

public interface IRadiationRepository {

    List<Radiation> find(IGaussKrueger gaussKrueger, int startDate, int endDate, String type, double lon, double lat);

    void save(List<Radiation> radiations);

    long count();
}
