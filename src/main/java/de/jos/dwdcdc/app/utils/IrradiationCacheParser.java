package de.jos.dwdcdc.app.utils;

import com.google.gson.Gson;
import de.jos.dwdcdc.app.entities.cache.IrradiationCache;
import de.jos.dwdcdc.app.interfaces.IIrradiationCache;
import de.jos.dwdcdc.app.interfaces.IIrradiationCacheParser;
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
  public IIrradiationCache getDbCache(String json) {
    return gson.fromJson(json, IrradiationCache.class);
  }

  @Override
  public String toJson(IIrradiationCache irradiationCache) {
    return gson.toJson(irradiationCache);
  }
}
