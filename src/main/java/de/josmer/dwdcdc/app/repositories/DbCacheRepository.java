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
public class DbCacheRepository extends Repository<DbCache> implements IDbCacheRepository<DbCache> {

    private final IDbCacheJsonParser parser;

    @Autowired
    public DbCacheRepository(IDbCacheJsonParser parser) {
        this.parser = parser;
    }

    @Override
    public Optional<DbCache> find(final String key) {
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
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(getSaveQuery(dbCache));
        } catch (SQLException | URISyntaxException e) {
            LOGGER.info(e.toString());
        }
    }

    @Override
    public void delete(String key) {

    }

    @Override
    protected DbCache mapTo(ResultSet rs) throws Exception {
        DbCache dbCache = parser.getDbCache(rs.getString("db_cache"));
        dbCache.setId(rs.getInt("id"));
        return dbCache;
    }

    private String getSaveQuery(DbCache dbCache) {
        return "INSERT INTO irradiation (id, db_cache) VALUES (" + dbCache.getId() + ",'" + getDbCache(dbCache) + "');";
    }

    private String getFindQuery(String key) {
        return "SELECT * FROM irradiation WHERE db_cache->>'key' = '" + key + "';";
    }

    private String getDbCache(DbCache dbCache) {
        return parser.toJson(dbCache);
    }
}
