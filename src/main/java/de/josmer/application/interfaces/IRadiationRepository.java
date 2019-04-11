package de.josmer.application.interfaces;

import de.josmer.application.entities.Radiation;

import java.util.List;

public interface IRadiationRepository {

    List<Radiation> find(int startDate, int endDate, String type, double lon, double lat);

    void save(List<Radiation> radiations);

    long count();
}
