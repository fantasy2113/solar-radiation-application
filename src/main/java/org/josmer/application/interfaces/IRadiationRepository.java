package org.josmer.application.interfaces;

import org.josmer.application.entities.Radiation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IRadiationRepository {

    List<Radiation> find(int startDate, int endDate, String type, double lon, double lat);

    void save(List<Radiation> radiations);

    boolean isConnected();

    long count();
}
