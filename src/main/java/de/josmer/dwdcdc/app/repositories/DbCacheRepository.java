package de.josmer.dwdcdc.app.repositories;

import de.josmer.dwdcdc.app.base.entities.cache.DbCache;
import de.josmer.dwdcdc.app.base.interfaces.IDbCacheJsonParser;
import de.josmer.dwdcdc.app.base.interfaces.IDbCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@Component
public class DbCacheRepository extends Repository<DbCache> implements IDbCacheRepository {

    private final IDbCacheJsonParser parser;

    @Autowired
    public DbCacheRepository(IDbCacheJsonParser parser) {
        this.parser = parser;
    }

    @Override
    public Optional<DbCache> get(String key) {
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
    public void save(DbCache dbCache) {
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
    protected DbCache mapTo(ResultSet rs) throws Exception {
        return parser.getDbCache(rs.getString("db_cache"));
    }

    private String getDbCache(DbCache dbCache) {
        return parser.toJson(dbCache);
    }

    private String getDeleteQuery(int id) {
        return "DELETE FROM irradiation WHERE id=" + id + ";";
    }

    private String getSaveQuery(DbCache dbCache) {
        return "INSERT INTO irradiation (id, db_cache) VALUES (" + dbCache.getId() + ",'"
                + getDbCache(dbCache) + "');";
    }

    private String getFindQuery(String key) {
        return "SELECT * FROM irradiation WHERE db_cache->>'key' = '" + key + "';";
    }
}
