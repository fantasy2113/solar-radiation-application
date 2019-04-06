package org.josmer.app.core;

import org.josmer.app.entity.Radiation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IMonthlyRadiationRepository {

    List<Radiation> find(int startDate, int endDate, String type, double lon, double lat);

    void save(List<Radiation> radiations);

    boolean isConnected();

    long count();
}
