package org.josmer.repositories;


import org.josmer.entities.Radiation;
import org.josmer.interfaces.IRadiationRepository;
import org.josmer.security.Connector;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class RadiationRepository implements IRadiationRepository {

    @Override
    public Optional<Radiation> get(final long id) {
        return Optional.empty();
    }

    @Override
    public List<Radiation> find(int startDate, int endDate, String typ, int yMin, int yMax, int xMin, int xMax) {
        return new ArrayList<>();
    }

    @Override
    public void save(final List<Radiation> radiations) {
        final String insertTableSQL = "INSERT INTO radiation (typ,date,x_min,x_max,y_min,y_max,value) VALUES (?,?,?,?,?,?,?)";
        Connection dbConnection = null;
        PreparedStatement preparedStatementInsert = null;
        try {
            try {
                dbConnection = DriverManager.getConnection(Connector.getUrl(), Connector.getUser(), Connector.getPassword());
                dbConnection.setAutoCommit(false);
                for (Radiation radiation : radiations) {
                    preparedStatementInsert = dbConnection.prepareStatement(insertTableSQL);
                    preparedStatementInsert.setString(1, radiation.getTyp());
                    preparedStatementInsert.setInt(2, radiation.getDate());
                    preparedStatementInsert.setInt(3, radiation.getxMin());
                    preparedStatementInsert.setInt(4, radiation.getxMax());
                    preparedStatementInsert.setInt(5, radiation.getyMin());
                    preparedStatementInsert.setInt(6, radiation.getyMax());
                    preparedStatementInsert.setDouble(7, radiation.getRadiation());
                    preparedStatementInsert.executeUpdate();
                }
                dbConnection.commit();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                dbConnection.rollback();
            } finally {
                if (preparedStatementInsert != null) {
                    preparedStatementInsert.close();
                }
                if (dbConnection != null) {
                    dbConnection.close();
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}