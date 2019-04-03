package org.josmer.interfaces;

import org.josmer.entities.Radiation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IRadiationRepository {

    List<Radiation> find(int startDate, int endDate, String typ, double lon, double lat);

    void save(List<Radiation> radiations);

    boolean isConnected();
}
