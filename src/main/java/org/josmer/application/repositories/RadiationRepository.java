package org.josmer.application.repositories;

import org.josmer.application.entities.Radiation;
import org.josmer.application.geo.GaussKrueger;
import org.josmer.application.interfaces.IRadiationRepository;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@Component
public final class RadiationRepository implements IRadiationRepository {

    private final String databaseUrl;

    public RadiationRepository(final String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    public RadiationRepository() {
        this.databaseUrl = System.getenv("DATABASE_URL");
    }

    @Override
    public List<Radiation> find(final int startDate, final int endDate, final String radiationType, final double lon, final double lat) {
        List<Radiation> radiations = new LinkedList<>();

        GaussKrueger gaussKrueger = new GaussKrueger(lon, lat);

        final int hochwert = getGkValues(gaussKrueger.getHochwert());

        int rechtswert;
        if (String.valueOf(gaussKrueger.getRechtswert()).startsWith("5")) {
            rechtswert = getGkValues(gaussKrueger.getRechtswert() - 1600000);
        } else if (String.valueOf(gaussKrueger.getRechtswert()).startsWith("4")) {
            rechtswert = getGkValues(gaussKrueger.getRechtswert() - 800000);
        } else if (String.valueOf(gaussKrueger.getRechtswert()).startsWith("3")) {
            rechtswert = getGkValues(gaussKrueger.getRechtswert());
        } else {
            return radiations;
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            try {
                connection = getConnection();
                preparedStatement = connection.prepareStatement("SELECT * FROM radiation WHERE radiation_date " + getInDates(startDate, endDate)
                        + " AND gkh_min = ? AND gkh_max = ? AND gkr_min = ? AND gkr_max = ? AND radiation_type = ? LIMIT ?;");
                preparedStatement.setInt(1, hochwert);
                preparedStatement.setInt(2, hochwert + 1000);
                preparedStatement.setInt(3, rechtswert);
                preparedStatement.setInt(4, rechtswert + 1000);
                preparedStatement.setString(5, radiationType);
                preparedStatement.setInt(6, endDate - startDate + 1);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    radiations.add(mapToRadiation(rs));
                }
            } catch (SQLException | URISyntaxException e) {
                System.out.println(e.getMessage());
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return radiations;
    }

    @Override
    public void save(final List<Radiation> radiations) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            try {
                connection = getConnection();
                connection.setAutoCommit(false);
                for (Radiation radiation : radiations) {
                    preparedStatement = connection.prepareStatement("INSERT INTO radiation (radiation_type,radiation_date,gkr_min,gkr_max,gkh_min,gkh_max,radiation_value) VALUES (?,?,?,?,?,?,?)");
                    preparedStatement.setString(1, radiation.getRadiationType());
                    preparedStatement.setInt(2, radiation.getRadiationDate());
                    preparedStatement.setInt(3, radiation.getGkrMin());
                    preparedStatement.setInt(4, radiation.getGkrMax());
                    preparedStatement.setInt(5, radiation.getGkhMin());
                    preparedStatement.setInt(6, radiation.getGkhMax());
                    preparedStatement.setFloat(7, radiation.getRadiationValue());
                    preparedStatement.executeUpdate();
                }
                connection.commit();
            } catch (SQLException | URISyntaxException e) {
                System.out.println(e.getMessage());
                if (connection != null) {
                    connection.rollback();
                }
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean isConnected() {
        try {
            Connection connection = getConnection();
            connection.close();
            return true;
        } catch (SQLException | URISyntaxException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public long count() {
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT reltuples::BIGINT AS estimate FROM pg_class WHERE relname='radiation';")) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException | URISyntaxException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    private Radiation mapToRadiation(ResultSet rs) throws SQLException {
        Radiation radiation = new Radiation();
        radiation.setRadiationType(rs.getString("radiation_type"));
        radiation.setRadiationDate(rs.getInt("radiation_date"));
        radiation.setGkrMin(rs.getInt("gkr_min"));
        radiation.setGkrMax(rs.getInt("gkr_max"));
        radiation.setGkhMin(rs.getInt("gkh_min"));
        radiation.setGkhMax(rs.getInt("gkh_max"));
        radiation.setRadiationValue(rs.getFloat("radiation_value"));
        return radiation;
    }

    private Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(databaseUrl);
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
        return DriverManager.getConnection(dbUrl, username, password);
    }

    private int getGkValues(final double value) {

        Integer decreasedValue = (int) value;
        int decreaseCounter = 0;
        while (!decreasedValue.toString().endsWith("500")) {
            decreasedValue--;
            decreaseCounter++;
        }

        /*Integer increasedValue = (int) value;
        int increaseCounter = 0;
        while (!increasedValue.toString().endsWith("500")) {
            increasedValue++;
            increaseCounter++;
        }*/

        return /*increaseCounter >= decreaseCounter ? */decreasedValue /*: increasedValue*/;
    }

    private String getInDates(final int startDate, final int endDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("IN (");
        for (int i = startDate; i <= endDate; i++) {
            sb.append(i);
            if (i != endDate) {
                sb.append(",");
            }
        }
        return sb.append(")").toString();
    }
}
