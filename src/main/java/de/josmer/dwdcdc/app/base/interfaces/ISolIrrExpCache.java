package de.josmer.dwdcdc.app.base.interfaces;

import de.josmer.dwdcdc.app.base.entities.cache.DbCache;
import de.josmer.dwdcdc.app.requests.IrrRequest;

import java.util.Optional;

public interface ISolIrrExpCache {
    void add(DbCache dbCache);

    Optional<DbCache> get(IrrRequest irrRequest);
}
