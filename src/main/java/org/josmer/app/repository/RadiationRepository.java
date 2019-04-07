package org.josmer.app.repository;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.josmer.app.core.IRadiationRepository;
import org.josmer.app.entity.Radiation;
import org.josmer.app.logic.utils.GaussKrueger;
import org.springframework.stereotype.Component;

@Component
public final class RadiationRepository implements IRadiationRepository {

    private final List<Integer> rwValues;
    private final String databaseUrl;

    public RadiationRepository(final String databaseUrl) {
        this.databaseUrl = databaseUrl;
        this.rwValues = getRwValues();

    }

    public RadiationRepository() {
        this.databaseUrl = System.getenv("DATABASE_URL");
        this.rwValues = getRwValues();
    }

    public List<Integer> getRwValues() {
        List<Integer> values = new ArrayList<>();
        int rw = 3280500;
        for (int i = 0; i < 654; i++) {
            values.add(rw += 4000);
        }
        return values;
    }

    @Override
    public List<Radiation> find(final int startDate, final int endDate, final String radiationType, final double lon, final double lat) {
        List<Radiation> radiations = new LinkedList<>();

        GaussKrueger gaussKrueger = new GaussKrueger(lon, lat);
        gaussKrueger.calculate();
        final int hochwert = getGkh(gaussKrueger.getHochwert());
        final int rechtswert = getGkr(gaussKrueger.getRechtswert());

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        final String statement = "SELECT * FROM radiation WHERE radiation_date IN " + getDates(startDate, endDate) + " AND gkh_min = ? AND gkh_max = ? AND gkr_min = ? AND gkr_max = ? AND radiation_type = ? LIMIT ?;";
        try {
            try {
                connection = getConnection();
                preparedStatement = connection.prepareStatement(statement);
                preparedStatement.setInt(1, hochwert);
                preparedStatement.setInt(2, hochwert + 1000);
                preparedStatement.setInt(3, rechtswert);
                preparedStatement.setInt(4, rechtswert + 4000);
                preparedStatement.setString(5, radiationType);
                preparedStatement.setInt(6, endDate - startDate + 1);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    radiations.add(mapToRadiation(rs));
                }
            } catch (SQLException | URISyntaxException e) {
                System.out.println(e);
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return radiations;
    }

    @Override
    public void save(final List<Radiation> radiations) {
        final String statement = "INSERT INTO radiation (radiation_type,radiation_date,gkr_min,gkr_max,gkh_min,gkh_max,radiation_value) VALUES (?,?,?,?,?,?,?)";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            try {
                connection = getConnection();
                connection.setAutoCommit(false);
                for (Radiation radiation : radiations) {
                    preparedStatement = connection.prepareStatement(statement);

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
                System.out.println(e);
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
            System.out.println(e);
        }
    }

    @Override
    public boolean isConnected() {
        try {
            Connection connection = getConnection();
            connection.close();
            return true;
        } catch (SQLException | URISyntaxException e) {
            System.out.println(e);
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
            System.out.println(e);
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

    private int getGkh(final double value) {
        Integer decrease = (int) value;
        Integer increase = (int) value;
        int decreaseCount = 0;
        int increaseCount = 0;
        while (isGkhStop(decrease)) {
            decrease--;
            decreaseCount++;
        }
        while (isGkhStop(increase)) {
            increase++;
            increaseCount++;
        }
        return increaseCount >= decreaseCount ? decrease : increase;
    }

    private int getGkr(final double value) {
        Integer decrease = (int) value;
        Integer increase = (int) value;
        int decreaseCount = 0;
        int increaseCount = 0;
        while (!this.rwValues.contains(decrease)) {
            decrease--;
            decreaseCount++;
        }
        while (!this.rwValues.contains(increase)) {
            increase++;
            increaseCount++;
        }
        return increaseCount >= decreaseCount ? decrease : increase;
    }

    private boolean isGkhStop(Integer increase) {
        return !increase.toString().endsWith("500");
    }

    private String getDates(final int startDate, final int endDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = startDate; i <= endDate; i++) {
            sb.append(i);
            if (i != endDate) {
                sb.append(",");
            }
        }
        return sb.append(")").toString();
    }
}
