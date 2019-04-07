package org.josmer.app.core;

import java.util.List;
import org.josmer.app.entity.Radiation;
import org.springframework.stereotype.Component;

@Component
public interface IRadiationRepository {

    List<Radiation> find(int startDate, int endDate, String type, double lon, double lat);

    void save(List<Radiation> radiations);

    boolean isConnected();

    long count();
}
