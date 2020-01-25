package de.josmer.dwdcdc.app.interfaces;

public interface IIrradiationCacheParser {

    IIrradiationCache getDbCache(String json);

    String toJson(IIrradiationCache irradiationCache);
}
