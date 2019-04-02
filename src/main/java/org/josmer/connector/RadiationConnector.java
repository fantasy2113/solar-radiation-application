package org.josmer.connector;

import org.josmer.entities.Radiation;
import org.josmer.interfaces.IRadiationConnector;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public class RadiationConnector implements IRadiationConnector {
    @Override
    public Radiation getEntity(PreparedStatement preparedStatement) throws SQLException {
        return null;
    }

    @Override
    public List<Radiation> getEntities(PreparedStatement preparedStatement) throws SQLException {
        return null;
    }

    @Override
    public int updateEntity(PreparedStatement preparedStatement) throws SQLException {
        return 0;
    }

    @Override
    public int saveAll(List<Radiation> entities) throws SQLException {
        return 0;
    }
}
