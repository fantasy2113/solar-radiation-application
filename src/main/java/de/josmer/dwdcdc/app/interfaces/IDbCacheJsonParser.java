package de.josmer.dwdcdc.app.interfaces;

import de.josmer.dwdcdc.app.entities.DbCache;

public interface IDbCacheJsonParser {
    String toJson(DbCache src);

    DbCache getDbCache(String json);
}
