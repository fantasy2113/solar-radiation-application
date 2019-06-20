package de.josmer.dwdcdc.app.cache;

import de.josmer.dwdcdc.app.base.entities.cache.IrradiationCache;
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
    private final ConcurrentHashMap<String, IrradiationCache> computedIrradiationCache;
    private final IIrradiationCaching irradiationCaching;

    @Autowired
    public IrradiationRamCaching(@Qualifier("IrradiationCacheDbCaching") IIrradiationCaching irradiationCaching) {
        this.computedIrradiationCache = new ConcurrentHashMap<>();
        this.irradiationCaching = irradiationCaching;
    }

    @Override
    public void add(IrradiationCache irradiationCache) {
        addCache(irradiationCache);
    }

    @Override
    public Optional<IrradiationCache> get(IrrRequest irrRequest) {
        Optional<IrradiationCache> optionalRamCache = getCache(irrRequest);
        if (optionalRamCache.isPresent()) {
            return optionalRamCache;
        }

        Optional<IrradiationCache> optionalDbCache = irradiationCaching.get(irrRequest);
        if (optionalDbCache.isPresent()) {
            addRamCache(optionalDbCache.get());
            return optionalDbCache;
        }

        return Optional.empty();
    }

    private void addRamCache(IrradiationCache dbCache) {
        cleanRamCache();
        computedIrradiationCache.putIfAbsent(dbCache.getKey(), dbCache);
    }

    private void addCache(IrradiationCache dbCache) {
        addRamCache(dbCache);
        irradiationCaching.add(dbCache);
    }

    private Optional<IrradiationCache> getCache(IrrRequest irrRequest) {
        return Optional.ofNullable(computedIrradiationCache.get(irrRequest.getKey()));
    }

    private void cleanRamCache() {
        if (computedIrradiationCache.size() >= LIMIT) {
            computedIrradiationCache.remove(computedIrradiationCache.keys().nextElement());
        }
    }
}
