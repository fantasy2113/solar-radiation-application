package org.josmer.application.interfaces;

import org.josmer.application.entities.Radiation;

import java.util.List;

public interface IRadiationRepository {

    List<Radiation> find(int startDate, int endDate, String type, double lon, double lat);

    void save(List<Radiation> radiations);

    boolean isConnected();

    long count();
}
