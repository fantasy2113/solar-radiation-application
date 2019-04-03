package org.josmer.repositories;


import org.josmer.entities.Radiation;
import org.josmer.interfaces.IRadiationRepository;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class RadiationRepository implements IRadiationRepository {

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
                dbConnection = getConnection();
                dbConnection.setAutoCommit(false);
                for (Radiation radiation : radiations) {
                    preparedStatementInsert = dbConnection.prepareStatement(insertTableSQL);
                    preparedStatementInsert.setString(1, radiation.getTyp());
                    preparedStatementInsert.setInt(2, radiation.getDate());
                    preparedStatementInsert.setInt(3, radiation.getxMin());
                    preparedStatementInsert.setInt(4, radiation.getxMax());
                    preparedStatementInsert.setInt(5, radiation.getyMin());
                    preparedStatementInsert.setInt(6, radiation.getyMax());
                    preparedStatementInsert.setFloat(7, radiation.getRadiation());
                    preparedStatementInsert.executeUpdate();
                }
                dbConnection.commit();
            } catch (SQLException | URISyntaxException e) {
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

    @Override
    public boolean isConnected() {
        try {
            Connection dbConnection = getConnection();
            dbConnection.close();
            return true;
        } catch (SQLException | URISyntaxException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    private Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    }
}