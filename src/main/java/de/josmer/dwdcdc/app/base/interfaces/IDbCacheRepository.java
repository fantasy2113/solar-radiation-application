package de.josmer.dwdcdc.app.base.interfaces;

import de.josmer.dwdcdc.app.base.entities.cache.DbCache;

import java.util.Optional;

public interface IDbCacheRepository {

    Optional<DbCache> get(String key);

    void save(DbCache dbCache);

    void delete(int id);
}
