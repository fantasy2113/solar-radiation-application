package de.josmer.dwdcdc.app.repositories;

import de.josmer.dwdcdc.app.base.entities.SolIrrExp;
import de.josmer.dwdcdc.app.base.entities.cache.DbCache;
import de.josmer.dwdcdc.app.base.interfaces.IDbCacheJsonParser;
import de.josmer.dwdcdc.app.base.interfaces.IDbCacheRepository;
import de.josmer.dwdcdc.app.base.interfaces.IJsonb;
import de.josmer.dwdcdc.app.requests.IrrRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Optional;

@Component
public class DbCacheRepository extends Repository<DbCache> implements IDbCacheRepository<IrrRequest> {

    private final IDbCacheJsonParser parser;

    @Autowired
    public DbCacheRepository(IDbCacheJsonParser parser) {
        this.parser = parser;
    }

    @Override
    public Optional<DbCache> find(IrrRequest item) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(getFindQuery(item.getKey()))) {
            if (rs.next()) {
                return Optional.of(mapTo(rs));
            }
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
        return Optional.empty();
    }

    @Override
    public void save(IrrRequest item, LinkedList<SolIrrExp> solIrrExps) {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(getSaveQuery(item, solIrrExps));
        } catch (SQLException | URISyntaxException e) {
            LOGGER.info(e.toString());
        }
    }

    @Override
    public void delete(IrrRequest item) {

    }

    @Override
    protected DbCache mapTo(ResultSet rs) throws Exception {
        return parser.getDbCache(rs.getString("db_cache"));
    }

    private String getSaveQuery(IJsonb item, LinkedList<SolIrrExp> solIrrExps) {
        DbCache dbCache = new DbCache(item.getKey(), solIrrExps);
        return "INSERT INTO irradiation (id, db_cache) VALUES (" + item.getId() + ",'"
                + getDbCache(dbCache) + "');";
    }

    private String getFindQuery(String key) {
        return "SELECT * FROM irradiation WHERE db_cache->>'key' = '" + key + "';";
    }

    private String getDbCache(DbCache dbCache) {
        return parser.toJson(dbCache);
    }
}
