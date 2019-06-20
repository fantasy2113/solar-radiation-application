package de.josmer.dwdcdc.app.base.interfaces;

import java.util.Optional;

public interface IDbCacheRepository<T extends IJsonb> {

    Optional<T> find(String key);

    void save(T dbCache);

    void delete(String key);
}
