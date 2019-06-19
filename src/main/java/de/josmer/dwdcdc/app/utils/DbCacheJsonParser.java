package de.josmer.dwdcdc.app.utils;

import com.google.gson.Gson;
import de.josmer.dwdcdc.app.entities.DbCache;
import de.josmer.dwdcdc.app.interfaces.IDbCacheJsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("gson")
public class DbCacheJsonParser implements IDbCacheJsonParser {
    private final Gson gson;

    @Autowired
    public DbCacheJsonParser(Gson gson) {
        this.gson = gson;
    }

    @Override
    public String toJson(DbCache src) {
        return gson.toJson(src);
    }

    @Override
    public DbCache getDbCache(String json) {
        return gson.fromJson(json, DbCache.class);
    }
}
