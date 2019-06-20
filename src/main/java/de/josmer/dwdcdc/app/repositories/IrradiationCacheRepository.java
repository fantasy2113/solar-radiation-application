package de.josmer.dwdcdc.app.repositories;

import de.josmer.dwdcdc.app.base.entities.cache.IrradiationCache;
import de.josmer.dwdcdc.app.base.interfaces.IIrradiationCacheParser;
import de.josmer.dwdcdc.app.base.interfaces.IIrradiationCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@Component
public class IrradiationCacheRepository extends Repository<IrradiationCache> implements IIrradiationCacheRepository {

    private final IIrradiationCacheParser parser;

    @Autowired
    public IrradiationCacheRepository(IIrradiationCacheParser parser) {
        this.parser = parser;
    }

    @Override
    public Optional<IrradiationCache> get(String key) {
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

    @Override
    public void save(IrradiationCache dbCache) {
        executeUpdate(getSaveQuery(dbCache));
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
    protected IrradiationCache mapTo(ResultSet rs) throws Exception {
        return parser.getDbCache(rs.getString("db_cache"));
    }

    private String getDbCache(IrradiationCache dbCache) {
        return parser.toJson(dbCache);
    }

    private String getDeleteQuery(int id) {
        return "DELETE FROM irradiation WHERE id=" + id + ";";
    }

    private String getSaveQuery(IrradiationCache dbCache) {
        return "INSERT INTO irradiation (id, db_cache) VALUES (" + dbCache.getId() + ",'"
                + getDbCache(dbCache) + "');";
    }

    private String getFindQuery(String key) {
        return "SELECT * FROM irradiation WHERE db_cache->>'key' = '" + key + "';";
    }
}
