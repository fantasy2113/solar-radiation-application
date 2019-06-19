package de.josmer.dwdcdc.app.interfaces;

import de.josmer.dwdcdc.app.entities.DbCache;

import java.util.Optional;

public interface IDbCacheRepository<T extends IJsonb> {

    Optional<DbCache> find(String key);

    void save(T dbCache);
}
