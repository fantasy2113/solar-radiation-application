package de.jos.dwdcdc.app.cache;

import de.jos.dwdcdc.app.interfaces.IIrradiationCache;
import de.jos.dwdcdc.app.interfaces.IIrradiationCaching;
import de.jos.dwdcdc.app.interfaces.Identifiable;
import de.jos.dwdcdc.app.spring.BeanNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component(BeanNames.IRRADIATION_RAM_CACHING)
public class IrradiationRamCaching implements IIrradiationCaching {
  private static final int LIMIT = 10000;
  private final IIrradiationCaching irradiationDbCaching;
  private final ConcurrentHashMap<String, IIrradiationCache> irradiationRamCache;

  @Autowired
  public IrradiationRamCaching(@Qualifier(BeanNames.IRRADIATION_DB_CACHING) IIrradiationCaching irradiationDbCaching) {
    this.irradiationRamCache = new ConcurrentHashMap<>();
    this.irradiationDbCaching = irradiationDbCaching;
  }

  @Override
  public void add(IIrradiationCache irradiationCache) {
    putCache(irradiationCache);
    irradiationDbCaching.add(irradiationCache);
  }

  private void cleanRamCache() {
    if (irradiationRamCache.size() >= LIMIT) {
      irradiationRamCache.remove(irradiationRamCache.keys().nextElement());
    }
  }

  @Override
  public Optional<IIrradiationCache> get(Identifiable identifiable) {
    return getIrradiationCache(identifiable).map(c -> loadCache(identifiable, c))
        .or(() -> Optional.ofNullable(getCacheFromDb(identifiable)));
  }

  private IIrradiationCache getCacheFromDb(Identifiable identifiable) {
    return irradiationDbCaching.get(identifiable).map(c -> {
      putCache(c);
      return c;
    }).orElse(null);
  }

  private Optional<IIrradiationCache> getIrradiationCache(Identifiable identifiable) {
    return Optional.ofNullable(irradiationRamCache.get(identifiable.getKey()));
  }

  private IIrradiationCache loadCache(Identifiable identifiable, IIrradiationCache irradiationCache) {
    if (isOldCache(irradiationCache)) {
      irradiationRamCache.remove(identifiable.getKey());
      return getCacheFromDb(identifiable);
    } else {
      return irradiationCache;
    }
  }

  private void putCache(IIrradiationCache irradiationCache) {
    cleanRamCache();
    irradiationRamCache.putIfAbsent(irradiationCache.getKey(), irradiationCache);
  }
}
