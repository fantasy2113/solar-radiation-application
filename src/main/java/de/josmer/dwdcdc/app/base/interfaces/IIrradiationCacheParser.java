package de.josmer.dwdcdc.app.base.interfaces;

import de.josmer.dwdcdc.app.base.entities.cache.IrradiationCache;

public interface IIrradiationCacheParser {
    String toJson(IrradiationCache irradiationCache);

    IrradiationCache getDbCache(String json);
}
