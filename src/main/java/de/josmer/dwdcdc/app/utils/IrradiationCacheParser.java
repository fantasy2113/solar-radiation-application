package de.josmer.dwdcdc.app.utils;

import com.google.gson.Gson;
import de.josmer.dwdcdc.app.base.interfaces.IIrradiationCache;
import de.josmer.dwdcdc.app.base.interfaces.IIrradiationCacheParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("gson")
public class IrradiationCacheParser implements IIrradiationCacheParser {
    private final Gson gson;

    @Autowired
    public IrradiationCacheParser(Gson gson) {
        this.gson = gson;
    }

    @Override
    public String toJson(IIrradiationCache irradiationCache) {
        return gson.toJson(irradiationCache);
    }

    @Override
    public IIrradiationCache getDbCache(String json) {
        return gson.fromJson(json, IIrradiationCache.class);
    }
}
