package de.josmer.app.model.repositories;

import de.josmer.app.model.entities.SolRadi;
import de.josmer.app.library.interfaces.IGaussKrueger;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.OptionalInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import de.josmer.app.library.interfaces.ISolRadiRepository;

@Component
public final class SolRadiRepository extends Repository<SolRadi> implements ISolRadiRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolRadiRepository.class.getName());

    public SolRadiRepository(final String databaseUrl) {
        super(databaseUrl);
    }

    public SolRadiRepository() {
        super();
    }

    @Override
    public double[] findGlobal(final IGaussKrueger gaussKrueger, final int startDate, final int endDate, final double lon, final double lat) {
        List<SolRadi> globalRadiation = find(gaussKrueger, startDate, endDate, "GLOBAL", lon, lat);
        double[] retArr = new double[12];
        try {
            for (int i = 0; i < retArr.length; i++) {
                retArr[i] = Double.parseDouble(String.format(Locale.ENGLISH, "%.2f", globalRadiation.get(i).getRadiationValue())) * 1000;
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return retArr;
    }

    @Override
    public List<SolRadi> find(final IGaussKrueger gaussKrueger, final int startDate, final int endDate, final String radiationType, final double lon, final double lat) {
        List<SolRadi> radiations = new LinkedList<>();
        gaussKrueger.convertFrom(lon, lat);
        final int hochwert = getGkValues(gaussKrueger.getHochwert());
        final OptionalInt optionalRechtswert = getRechtswert(gaussKrueger);
        if (optionalRechtswert.isEmpty()) {
            return radiations;
        }
        try (Connection connection = getConnection();
                PreparedStatement preparedStatement
                = connection.prepareStatement("SELECT * FROM radiation WHERE radiation_date " + getInDates(startDate, endDate)
                        + " AND gkh_min = ? AND gkh_max = ? AND gkr_min = ? AND gkr_max = ? AND radiation_type = ? ORDER BY radiation_date ASC LIMIT ?;")) {
            preparedStatement.setInt(1, hochwert);
            preparedStatement.setInt(2, hochwert + 1000);
            preparedStatement.setInt(3, optionalRechtswert.getAsInt());
            preparedStatement.setInt(4, optionalRechtswert.getAsInt() + 1000);
            preparedStatement.setString(5, radiationType);
            preparedStatement.setInt(6, endDate - startDate + 1);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    radiations.add(mapToEntity(rs));
                }
            }
        } catch (SQLException | URISyntaxException e) {
            LOGGER.info(e.getMessage());
        }
        return radiations;
    }

    private OptionalInt getRechtswert(IGaussKrueger gaussKrueger) {
        if (String.valueOf(gaussKrueger.getRechtswert()).startsWith("5")) {
            return OptionalInt.of(getGkValues(gaussKrueger.getRechtswert() - 1600000));
        } else if (String.valueOf(gaussKrueger.getRechtswert()).startsWith("4")) {
            return OptionalInt.of(getGkValues(gaussKrueger.getRechtswert() - 800000));
        } else if (String.valueOf(gaussKrueger.getRechtswert()).startsWith("3")) {
            return OptionalInt.of(getGkValues(gaussKrueger.getRechtswert()));
        }
        return OptionalInt.empty();
    }

    @Override
    public void save(final List<SolRadi> radiations) {
        try (Connection connection = getConnection();
                PreparedStatement preparedStatement
                = connection.prepareStatement("INSERT INTO radiation (radiation_type,radiation_date,gkr_min,gkr_max,gkh_min,gkh_max,radiation_value) VALUES (?,?,?,?,?,?,?)")) {
            connection.setAutoCommit(false);
            for (SolRadi radiation : radiations) {
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
            LOGGER.info("insert month");
        } catch (SQLException | URISyntaxException e) {
            LOGGER.info(e.getMessage());
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
            LOGGER.info(e.getMessage());
        }
        return -1;
    }

    @Override
    protected SolRadi mapToEntity(ResultSet rs) throws SQLException {
        SolRadi radiation = new SolRadi();
        radiation.setRadiationType(rs.getString("radiation_type"));
        radiation.setRadiationDate(rs.getInt("radiation_date"));
        radiation.setGkrMin(rs.getInt("gkr_min"));
        radiation.setGkrMax(rs.getInt("gkr_max"));
        radiation.setGkhMin(rs.getInt("gkh_min"));
        radiation.setGkhMax(rs.getInt("gkh_max"));
        radiation.setRadiationValue(rs.getFloat("radiation_value"));
        return radiation;
    }

    private int getGkValues(final double value) {
        Integer gkMin = (int) value;
        while (!gkMin.toString().endsWith("500")) {
            gkMin--;
        }
        return gkMin;
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