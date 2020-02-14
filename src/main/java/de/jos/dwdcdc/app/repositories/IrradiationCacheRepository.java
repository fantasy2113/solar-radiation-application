package de.jos.dwdcdc.app.repositories;

import de.jos.dwdcdc.app.interfaces.IIrradiationCache;
import de.jos.dwdcdc.app.interfaces.IIrradiationCacheParser;
import de.jos.dwdcdc.app.interfaces.IIrradiationCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@Component
public class IrradiationCacheRepository extends Repository<IIrradiationCache> implements IIrradiationCacheRepository {

    private final IIrradiationCacheParser parser;

    @Autowired
    public IrradiationCacheRepository(IIrradiationCacheParser parser) {
        this.parser = parser;
    }

    @Override
    public void delete(int id) {
        executeUpdate(getDeleteQuery(id));
    }

    private void executeUpdate(String query) {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException | URISyntaxException e) {
            LOGGER.info(e.toString());
        }
    }

    @Override
    public Optional<IIrradiationCache> get(String key) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(getFindQuery(key))) {
            if (rs.next()) {
                return Optional.of(mapTo(rs));
            }
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
        return Optional.empty();
    }

    private String getDbCache(IIrradiationCache irradiationCache) {
        return parser.toJson(irradiationCache);
    }

    private String getDeleteQuery(int id) {
        return "DELETE FROM irradiation WHERE id=" + id + ";";
    }

    private String getFindQuery(String key) {
        return "SELECT * FROM irradiation WHERE db_cache->>'key' = '" + key + "';";
    }

    private String getSaveQuery(IIrradiationCache irradiationCache) {
        return "INSERT INTO irradiation (id, db_cache) VALUES (" + irradiationCache.getId() + ",'"
                + getDbCache(irradiationCache) + "');";
    }

    @Override
    protected IIrradiationCache mapTo(ResultSet rs) throws Exception {
        return parser.getDbCache(rs.getString("db_cache"));
    }

    @Override
    public void save(IIrradiationCache irradiationCache) {
        executeUpdate(getSaveQuery(irradiationCache));
    }
}
