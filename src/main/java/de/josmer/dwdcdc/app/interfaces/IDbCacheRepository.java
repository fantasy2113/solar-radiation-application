package de.josmer.dwdcdc.app.interfaces;

import de.josmer.dwdcdc.app.entities.DbCache;
import de.josmer.dwdcdc.app.entities.SolIrrExp;

import java.util.LinkedList;
import java.util.Optional;

public interface IDbCacheRepository {

    Optional<LinkedList<SolIrrExp>> find(String key);

    void save(DbCache dbCache);
}
