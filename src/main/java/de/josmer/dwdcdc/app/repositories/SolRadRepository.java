package de.josmer.dwdcdc.app.repositories;

import de.josmer.dwdcdc.app.entities.SolRad;
import de.josmer.dwdcdc.app.interfaces.ISolRadRepository;
import de.josmer.dwdcdc.library.enums.SolRadTypes;
import de.josmer.dwdcdc.library.geotrans.GaussKruger;
import de.josmer.dwdcdc.library.geotrans.GkConverter;
import de.josmer.dwdcdc.library.interfaces.ISolRad;
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

    public SolRadRepository() {
        super();
    }

    public SolRadRepository(final String databaseUrl) {
        super(databaseUrl);
    }

    private void checkRadiationValue(SolRad radiation, float radiationValue) {
        if (radiationValue > 0) {
            radiation.setRadiationValue(radiationValue);
        } else {
            radiation.setRadiationValue(0);
        }
    }

    private double convertValue(double value) {
        return Double.parseDouble(String.format(Locale.ENGLISH, "%.2f", value)) * 1000;
    }

    private void executeQuery(LinkedList<SolRad> radiations, PreparedStatement prepStmt) throws SQLException {
        try (ResultSet rs = prepStmt.executeQuery()) {
            while (rs.next()) {
                radiations.add(mapTo(rs));
            }
        }
    }

    private void executeUpdate(PreparedStatement prepStmt, ISolRad solRad) throws SQLException {
        prepStmt.setString(1, solRad.getRadiationType());
        prepStmt.setInt(2, solRad.getRadiationDate());
        prepStmt.setInt(3, solRad.getGkrMin());
        prepStmt.setInt(4, solRad.getGkrMax());
        prepStmt.setInt(5, solRad.getGkhMin());
        prepStmt.setInt(6, solRad.getGkhMax());
        prepStmt.setFloat(7, solRad.getRadiationValue());
        prepStmt.executeUpdate();
    }

    private void executeUpdates(List<ISolRad> radiations, PreparedStatement prepStmt) throws SQLException {
        for (ISolRad solRad : radiations) {
            executeUpdate(prepStmt, solRad);
        }
    }

    @Override
    public LinkedList<SolRad> find(final int startDate, final int endDate, final SolRadTypes solRadTypes,
                                   final double lon, final double lat) {
        LinkedList<SolRad> radiations = new LinkedList<>();
        GkConverter gkConverter = new GkConverter(new GaussKruger(lon, lat));

        final int hochwert = gkConverter.getHochwert();
        final OptionalInt optionalRechtswert = gkConverter.getRechtswert();

        if (optionalRechtswert.isEmpty()) {
            return radiations;
        }

        try (Connection conn = getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(getSelectSql(startDate, endDate))) {
            setSelectStmt(startDate, endDate, solRadTypes, hochwert, optionalRechtswert.getAsInt(), prepStmt);
            executeQuery(radiations, prepStmt);
        } catch (SQLException | URISyntaxException e) {
            LOGGER.info(e.toString());
        }
        return radiations;
    }

    @Override
    public double[] findGlobal(final int startDate, final int endDate, final double lon, final double lat) {
        LinkedList<SolRad> solRads = find(startDate, endDate, SolRadTypes.GLOBAL, lon, lat);
        return solRads.stream().sequential().map(SolRad::getRadiationValue).mapToDouble(this::convertValue).toArray();
    }

    private String getInDates(final int startDate, final int endDate) {
        StringBuilder sb = new StringBuilder("IN (");
        IntStream.range(startDate, endDate + 1).forEach(d -> {
            sb.append(d);
            sb.append(",");
        });
        return sb.append(")").toString().replace(",)", ")");
    }

    private String getInsertSql() {
        return "INSERT INTO radiation (radiation_type,radiation_date,gkr_min,gkr_max,gkh_min,gkh_max,radiation_value) VALUES (?,?,?,?,?,?,?)";
    }

    private int getLimit(int startDate, int endDate) {
        return endDate - startDate + 1;
    }

    @Override
    public int getNumberOfRadiations() {
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT reltuples::BIGINT AS estimate FROM pg_class WHERE relname='radiation';")) {
            if (rs.next()) {
                return (int) rs.getLong(1);
            }
        } catch (SQLException | URISyntaxException e) {
            LOGGER.info(e.toString());
        }
        return -1;
    }

    private String getSelectSql(int startDate, int endDate) {
        return "SELECT * FROM radiation WHERE radiation_date " + getInDates(startDate, endDate)
                + " AND gkh_min = ? AND gkh_max = ? AND gkr_min = ? AND gkr_max = ? AND radiation_type = ? ORDER BY radiation_date ASC LIMIT ?;";
    }

    @Override
    public boolean isAlreadyExist(int date, SolRadTypes solRadTypes) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM radiation WHERE radiation_date = ? AND radiation_type = ? LIMIT 1;")) {
            preparedStatement.setInt(1, date);
            preparedStatement.setString(2, solRadTypes.name());
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException | URISyntaxException e) {
            LOGGER.info(e.toString());
        }
        return false;
    }

    @Override
    protected SolRad mapTo(ResultSet rs) throws SQLException {
        SolRad solRad = new SolRad();
        solRad.setRadiationType(rs.getString("radiation_type"));
        solRad.setRadiationDate(rs.getInt("radiation_date"));
        solRad.setGkrMin(rs.getInt("gkr_min"));
        solRad.setGkrMax(rs.getInt("gkr_max"));
        solRad.setGkhMin(rs.getInt("gkh_min"));
        solRad.setGkhMax(rs.getInt("gkh_max"));
        checkRadiationValue(solRad, rs.getFloat("radiation_value"));
        return solRad;
    }

    @Override
    public void save(final List<ISolRad> radiations) {
        if (radiations.isEmpty()) {
            return;
        }
        try (Connection conn = getConnection(); PreparedStatement prepStmt = conn.prepareStatement(getInsertSql())) {
            conn.setAutoCommit(false);
            executeUpdates(radiations, prepStmt);
            conn.commit();
        } catch (SQLException | URISyntaxException e) {
            LOGGER.info(e.toString());
        }
    }

    private void setSelectStmt(int startDate, int endDate, SolRadTypes solRadTypes, int hochwert, int rechtswert,
                               PreparedStatement prepStmt) throws SQLException {
        prepStmt.setInt(1, hochwert);
        prepStmt.setInt(2, hochwert + 1000);
        prepStmt.setInt(3, rechtswert);
        prepStmt.setInt(4, rechtswert + 1000);
        prepStmt.setString(5, solRadTypes.name());
        prepStmt.setInt(6, getLimit(startDate, endDate));
    }
}
