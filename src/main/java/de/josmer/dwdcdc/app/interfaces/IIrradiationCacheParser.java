package de.josmer.dwdcdc.app.interfaces;

public interface IIrradiationCacheParser {
    String toJson(IIrradiationCache irradiationCache);

    IIrradiationCache getDbCache(String json);
}
