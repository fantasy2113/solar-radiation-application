package de.josmer.dwdcdc.app.repositories;

import de.josmer.dwdcdc.app.interfaces.ISolRadRepository;
import de.josmer.dwdcdc.utils.entities.SolRad;
import de.josmer.dwdcdc.utils.enums.SolRadTypes;
import de.josmer.dwdcdc.utils.geo.GaussKruger;
import de.josmer.dwdcdc.utils.geo.GkConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.OptionalInt;
import java.util.stream.IntStream;

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
    public boolean isAlreadyExist(int date, SolRadTypes solRadTypes) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM radiation WHERE radiation_date = ? AND radiation_type = ? LIMIT 1;")) {
            preparedStatement.setInt(1, date);
            preparedStatement.setString(2, solRadTypes.name());
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException | URISyntaxException e) {
            LOGGER.info(e.getMessage());
        }
        return false;
    }

    @Override
    public double[] findGlobal(final int startDate, final int endDate, final double lon, final double lat) {
        LinkedList<SolRad> solRads = find(startDate, endDate, SolRadTypes.GLOBAL, lon, lat);
        return solRads.stream().sequential().map(SolRad::getRadiationValue)
                .mapToDouble(this::convertValue).toArray();
    }

    @Override
    public LinkedList<SolRad> find(final int startDate, final int endDate, final SolRadTypes solRadTypes, final double lon, final double lat) {
        LinkedList<SolRad> radiations = new LinkedList<>();
        GkConverter gkConverter = new GkConverter(new GaussKruger(lon, lat));

        final int hochwert = gkConverter.getHochwert();
        final OptionalInt optionalRechtswert = gkConverter.getRechtswert();

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
            preparedStatement.setString(5, solRadTypes.name());
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

    @Override
    public void save(final List<SolRad> radiations) {
        if (radiations.isEmpty()) {
            return;
        }

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

        final float radiationValue = rs.getFloat("radiation_value");

        if (radiationValue > 0) {
            radiation.setRadiationValue(radiationValue);
        } else {
            radiation.setRadiationValue(0);
        }

        return radiation;
    }

    private String getInDates(final int startDate, final int endDate) {
        StringBuilder sb = new StringBuilder("IN (");
        IntStream.range(startDate, endDate + 1).forEach(d -> {
            sb.append(d);
            sb.append(",");
        });
        return sb.append(")").toString().replace(",)", ")");
    }

    private double convertValue(double value) {
        return Double.parseDouble(String.format(Locale.ENGLISH, "%.2f", value)) * 1000;
    }
}
