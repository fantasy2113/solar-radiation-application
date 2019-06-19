package de.josmer.dwdcdc.app.repositories;

import de.josmer.dwdcdc.app.entities.DbCache;
import de.josmer.dwdcdc.app.interfaces.IDbCacheJsonParser;
import de.josmer.dwdcdc.app.interfaces.IDbCacheRepository;
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
    public Optional<DbCache> find(final String key) {
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

    private String getSaveQuery(DbCache dbCache) {
        return "INSERT INTO irradiation VALUES ('" + parser.toJson(dbCache) + "');";
    }

    @Override
    protected DbCache mapToEntity(ResultSet rs) throws SQLException {
        return null;
    }
}
