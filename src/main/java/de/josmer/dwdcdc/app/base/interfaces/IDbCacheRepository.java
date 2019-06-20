package de.josmer.dwdcdc.app.base.interfaces;

import de.josmer.dwdcdc.app.base.entities.SolIrrExp;
import de.josmer.dwdcdc.app.base.entities.cache.DbCache;

import java.util.LinkedList;
import java.util.Optional;

public interface IDbCacheRepository<T extends IJsonb> {

    Optional<DbCache> find(T item);

    void save(T item, LinkedList<SolIrrExp> solIrrExps);

    void delete(T item);
}
