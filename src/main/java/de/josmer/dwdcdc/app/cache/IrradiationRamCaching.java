package de.josmer.dwdcdc.app.cache;

import de.josmer.dwdcdc.app.interfaces.IIrradiationCache;
import de.josmer.dwdcdc.app.interfaces.IIrradiationCaching;
import de.josmer.dwdcdc.app.interfaces.Identifiable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component("IrradiationRamCaching")
public class IrradiationRamCaching implements IIrradiationCaching {

    private static final int LIMIT = 10000;
    private final ConcurrentHashMap<String, IIrradiationCache> irradiationRamCache;
    private final IIrradiationCaching irradiationDbCaching;

    @Autowired
    public IrradiationRamCaching(@Qualifier("IrradiationDbCaching") IIrradiationCaching irradiationDbCaching) {
        this.irradiationRamCache = new ConcurrentHashMap<>();
        this.irradiationDbCaching = irradiationDbCaching;
    }

    @Override
    public void add(IIrradiationCache irradiationCache) {
        putCache(irradiationCache);
        irradiationDbCaching.add(irradiationCache);
    }

    @Override
    public Optional<IIrradiationCache> get(Identifiable identifiable) {
        return getIrradiationCache(identifiable).map(c -> loadCache(identifiable, c))
                .or(() -> Optional.ofNullable(getCacheFromDb(identifiable)));
    }

    private IIrradiationCache loadCache(Identifiable identifiable, IIrradiationCache irradiationCache) {
        if (isOldCache(irradiationCache)) {
            irradiationRamCache.remove(identifiable.getKey());
            return getCacheFromDb(identifiable);
        } else {
            return irradiationCache;
        }
    }

    private IIrradiationCache getCacheFromDb(Identifiable identifiable) {
        return irradiationDbCaching.get(identifiable).map(c -> {
            putCache(c);
            return c;
        }).orElse(null);
    }

    private void putCache(IIrradiationCache irradiationCache) {
        cleanRamCache();
        irradiationRamCache.putIfAbsent(irradiationCache.getKey(), irradiationCache);
    }

    private Optional<IIrradiationCache> getIrradiationCache(Identifiable identifiable) {
        return Optional.ofNullable(irradiationRamCache.get(identifiable.getKey()));
    }

    private void cleanRamCache() {
        if (irradiationRamCache.size() >= LIMIT) {
            irradiationRamCache.remove(irradiationRamCache.keys().nextElement());
        }
    }
}
