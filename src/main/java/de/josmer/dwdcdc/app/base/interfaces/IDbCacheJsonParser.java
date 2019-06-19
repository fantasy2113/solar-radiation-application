package de.josmer.dwdcdc.app.base.interfaces;

import de.josmer.dwdcdc.app.base.entities.cache.DbCache;

public interface IDbCacheJsonParser {
    String toJson(DbCache src);

    DbCache getDbCache(String json);
}
