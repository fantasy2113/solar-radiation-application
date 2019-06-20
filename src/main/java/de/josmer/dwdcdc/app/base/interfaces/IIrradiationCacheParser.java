package de.josmer.dwdcdc.app.base.interfaces;

public interface IIrradiationCacheParser {
    String toJson(IIrradiationCache irradiationCache);

    IIrradiationCache getDbCache(String json);
}
