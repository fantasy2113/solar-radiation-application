package de.josmer.dwdcdc.app.repositories;

import de.josmer.dwdcdc.app.entities.DbCache;
import de.josmer.dwdcdc.app.interfaces.IDbCacheJsonParser;
import de.josmer.dwdcdc.app.interfaces.IDbCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DbCacheRepository implements IDbCacheRepository {

    private final IDbCacheJsonParser dbCacheJsonParser;

    @Autowired
    public DbCacheRepository(IDbCacheJsonParser dbCacheJsonParser) {
        this.dbCacheJsonParser = dbCacheJsonParser;
    }

    @Override
    public Optional<DbCache> find(final String key) {
        return Optional.empty();
    }

    @Override
    public void save(DbCache dbCache) {

    }
}
