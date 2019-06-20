package de.josmer.dwdcdc.app.utils;

import com.google.gson.Gson;
import de.josmer.dwdcdc.app.base.entities.cache.IrradiationCache;
import de.josmer.dwdcdc.app.base.interfaces.IIrradiationCacheParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("gson")
public class DbCacheJsonParser implements IIrradiationCacheParser {
    private final Gson gson;

    @Autowired
    public DbCacheJsonParser(Gson gson) {
        this.gson = gson;
    }

    @Override
    public String toJson(IrradiationCache irradiationCache) {
        return gson.toJson(irradiationCache);
    }

    @Override
    public IrradiationCache getDbCache(String json) {
        return gson.fromJson(json, IrradiationCache.class);
    }
}
