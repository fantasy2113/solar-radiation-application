package org.josmer.app.core;

import org.josmer.app.entity.Radiation;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
public interface IRadiationRepository {

    List<Radiation> find(int startDate, int endDate, String typ, double lon, double lat);

    void save(List<Radiation> radiations) throws SQLException;

    boolean isConnected();
}
