package org.josmer.repositories;


import org.josmer.entities.Radiation;
import org.josmer.interfaces.IRadiationRepository;
import org.josmer.utils.GeoToGk;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@Component
public class RadiationRepository implements IRadiationRepository {

    @Override
    public List<Radiation> find(final int startDate, final int endDate, final String typ, final double lon, final double lat) {
        List<Radiation> radiations = new LinkedList<>();
        GeoToGk geoToGk = new GeoToGk(lon, lat);
        geoToGk.calculate();
        final int hochwert = getValue(geoToGk.getHochwert());
        final int rechtswert = getValue(geoToGk.getRechtswert());
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        final String statement = "SELECT * FROM radiation WHERE y_min = ? AND y_max = ? AND x_min = ? AND x_max = ? AND date IN (" + getIn(startDate, endDate) + ") AND typ = ? LIMIT ?;";
        try {
            try {
                connection = getConnection();
                preparedStatement = connection.prepareStatement(statement);
                preparedStatement.setInt(1, hochwert);
                preparedStatement.setInt(2, hochwert + 1000);
                preparedStatement.setInt(3, rechtswert);
                preparedStatement.setInt(4, rechtswert + 1000);
                preparedStatement.setString(5, typ);
                preparedStatement.setInt(6, endDate - startDate + 1);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    radiations.add(mapToRadiation(rs));
                }
            } catch (SQLException | URISyntaxException e) {
                System.err.println(e.getMessage());
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return radiations;
    }

    private Radiation mapToRadiation(ResultSet rs) throws SQLException {
        Radiation radiation = new Radiation();
        radiation.setTyp(rs.getString("typ"));
        radiation.setDate(rs.getInt("date"));
        radiation.setxMin(rs.getInt("x_min"));
        radiation.setxMax(rs.getInt("x_max"));
        radiation.setyMin(rs.getInt("y_min"));
        radiation.setyMax(rs.getInt("y_min"));
        radiation.setValue(rs.getInt("value"));
        return radiation;
    }

    @Override
    public void save(final List<Radiation> radiations) {
        final String statement = "INSERT INTO radiation (typ,date,x_min,x_max,y_min,y_max,value) VALUES (?,?,?,?,?,?,?)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            try {
                connection = getConnection();
                connection.setAutoCommit(false);
                for (Radiation radiation : radiations) {
                    preparedStatement = connection.prepareStatement(statement);
                    preparedStatement.setString(1, radiation.getTyp());
                    preparedStatement.setInt(2, radiation.getDate());
                    preparedStatement.setInt(3, radiation.getxMin());
                    preparedStatement.setInt(4, radiation.getxMax());
                    preparedStatement.setInt(5, radiation.getyMin());
                    preparedStatement.setInt(6, radiation.getyMax());
                    preparedStatement.setFloat(7, radiation.getValue());
                    preparedStatement.executeUpdate();
                }
                connection.commit();
            } catch (SQLException | URISyntaxException e) {
                System.err.println(e.getMessage());
                connection.rollback();
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public boolean isConnected() {
        try {
            Connection connection = getConnection();
            connection.close();
            return true;
        } catch (SQLException | URISyntaxException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
        return DriverManager.getConnection(dbUrl, username, password);
    }

    private int getValue(final double value) {
        Integer decrease = (int) value;
        Integer increase = (int) value;
        int decreaseCount = 0;
        int increaseCount = 0;
        while (!decrease.toString().endsWith("500")) {
            decrease--;
            decreaseCount++;
        }
        while (!increase.toString().endsWith("500")) {
            increase++;
            increaseCount++;
        }
        return increaseCount >= decreaseCount ? decrease : increase;
    }

    private String getIn(final int startDate, final int endDate) {
        StringBuilder sb = new StringBuilder();
        for (int i = startDate; i <= endDate; i++) {
            sb.append(i);
            if (i != endDate) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}