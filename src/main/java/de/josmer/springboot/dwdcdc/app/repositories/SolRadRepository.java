package de.josmer.springboot.dwdcdc.app.repositories;

import de.josmer.springboot.dwdcdc.app.entities.SolRad;
import de.josmer.springboot.dwdcdc.app.geo.GaussKruger;
import de.josmer.springboot.dwdcdc.app.interfaces.ISolRadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.OptionalInt;

@Component
public final class SolRadRepository extends Repository<SolRad> implements ISolRadRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(SolRadRepository.class.getName());

    public SolRadRepository(final String databaseUrl) {
        super(databaseUrl);
    }

    public SolRadRepository() {
        super();
    }

    @Override
    public double[] findGlobal(final int startDate, final int endDate, final double lon, final double lat) {
        return find(startDate, endDate, "GLOBAL", lon, lat)
                .stream().sequential().map(SolRad::getRadiationValue)
                .mapToDouble(this::convertValue).toArray();
    }

    @Override
    public List<SolRad> find(final int startDate, final int endDate, final String radiationType, final double lon, final double lat) {
        List<SolRad> radiations = new LinkedList<>();
        GaussKruger gaussKrueger = new GaussKruger(lon, lat);
        gaussKrueger.compute();
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

    private OptionalInt getRechtswert(GaussKruger gaussKrueger) {
        if (String.valueOf(gaussKrueger.getRechtswert()).startsWith("5")) {
            return OptionalInt.of(getGkValues(gaussKrueger.getRechtswert() - 1600000));
        } else if (String.valueOf(gaussKrueger.getRechtswert()).startsWith("4")) {
            return OptionalInt.of(getGkValues(gaussKrueger.getRechtswert() - 800000));
        } else if (String.valueOf(gaussKrueger.getRechtswert()).startsWith("3")) {
            return OptionalInt.of(getGkValues(gaussKrueger.getRechtswert()));
        } else if (String.valueOf(gaussKrueger.getRechtswert()).startsWith("2")) {
            return OptionalInt.of(getGkValues(gaussKrueger.getRechtswert() + 800000));
        }
        return OptionalInt.empty();
    }

    @Override
    public void save(final List<SolRad> radiations) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement
                     = connection.prepareStatement("INSERT INTO radiation (radiation_type,radiation_date,gkr_min,gkr_max,gkh_min,gkh_max,radiation_value) VALUES (?,?,?,?,?,?,?)")) {
            connection.setAutoCommit(false);
            for (SolRad radiation : radiations) {
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
            LOGGER.info("inserting month");
        } catch (SQLException | URISyntaxException e) {
            LOGGER.info(e.getMessage());
        }
    }

    @Override
    public long getNumberOfRadiations() {
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
    protected SolRad mapToEntity(ResultSet rs) throws SQLException {
        SolRad radiation = new SolRad();
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

    private double convertValue(double value) {
        return Double.parseDouble(String.format(Locale.ENGLISH, "%.2f", value)) * 1000;
    }
}
