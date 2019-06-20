package de.josmer.dwdcdc.app.cache;

import de.josmer.dwdcdc.app.base.interfaces.IIrradiationCache;
import de.josmer.dwdcdc.app.base.interfaces.IIrradiationCaching;
import de.josmer.dwdcdc.app.requests.IrrRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component("IrradiationRamCaching")
public class IrradiationRamCaching implements IIrradiationCaching {
    private static final int LIMIT = 10000;
    private final ConcurrentHashMap<String, IIrradiationCache> irradiationRamCache;
    private final IIrradiationCaching irradiationCaching;

    @Autowired
    public IrradiationRamCaching(@Qualifier("IrradiationDbCaching") IIrradiationCaching irradiationCaching) {
        this.irradiationRamCache = new ConcurrentHashMap<>();
        this.irradiationCaching = irradiationCaching;
    }

    @Override
    public void add(IIrradiationCache irradiationCache) {
        putCache(irradiationCache);
        irradiationCaching.add(irradiationCache);
    }

    @Override
    public Optional<IIrradiationCache> get(IrrRequest irrRequest) {
        return getIrradiationCache(irrRequest).map(c -> loadCache(irrRequest, c));
    }

    private IIrradiationCache loadCache(IrrRequest irrRequest, IIrradiationCache irradiationCache) {
        if (isOldCache(irradiationCache)) {
            irradiationRamCache.remove(irrRequest.getKey());
            return getCacheFromDb(irrRequest);
        } else {
            return irradiationCache;
        }
    }

    private IIrradiationCache getCacheFromDb(IrrRequest irrRequest) {
        return irradiationCaching.get(irrRequest).map(c -> {
            putCache(c);
            return c;
        }).orElse(null);
    }

    private void putCache(IIrradiationCache irradiationCache) {
        cleanRamCache();
        irradiationRamCache.putIfAbsent(irradiationCache.getKey(), irradiationCache);
    }

    private Optional<IIrradiationCache> getIrradiationCache(IrrRequest irrRequest) {
        return Optional.ofNullable(irradiationRamCache.get(irrRequest.getKey()));
    }

    private void cleanRamCache() {
        if (irradiationRamCache.size() >= LIMIT) {
            irradiationRamCache.remove(irradiationRamCache.keys().nextElement());
        }
    }
}
