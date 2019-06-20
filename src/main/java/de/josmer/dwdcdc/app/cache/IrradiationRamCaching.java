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
    private final ConcurrentHashMap<String, IIrradiationCache> computedIrradiationCache;
    private final IIrradiationCaching irradiationCaching;

    @Autowired
    public IrradiationRamCaching(@Qualifier("IrradiationDbCaching") IIrradiationCaching irradiationCaching) {
        this.computedIrradiationCache = new ConcurrentHashMap<>();
        this.irradiationCaching = irradiationCaching;
    }

    @Override
    public void add(IIrradiationCache irradiationCache) {
        addCache(irradiationCache);
    }

    @Override
    public Optional<IIrradiationCache> get(IrrRequest irrRequest) {
        Optional<IIrradiationCache> optionalRamCache = getCache(irrRequest);
        if (optionalRamCache.isPresent()) {
            return optionalRamCache;
        }
        Optional<IIrradiationCache> optionalDbCache = irradiationCaching.get(irrRequest);
        if (optionalDbCache.isPresent()) {
            addRamCache(optionalDbCache.get());
            return optionalDbCache;
        }
        return Optional.empty();
    }

    private void addRamCache(IIrradiationCache irradiationCache) {
        cleanRamCache();
        computedIrradiationCache.putIfAbsent(irradiationCache.getKey(), irradiationCache);
    }

    private void addCache(IIrradiationCache irradiationCache) {
        addRamCache(irradiationCache);
        irradiationCaching.add(irradiationCache);
    }

    private Optional<IIrradiationCache> getCache(IrrRequest irrRequest) {
        return Optional.ofNullable(computedIrradiationCache.get(irrRequest.getKey()));
    }

    private void cleanRamCache() {
        if (computedIrradiationCache.size() >= LIMIT) {
            computedIrradiationCache.remove(computedIrradiationCache.keys().nextElement());
        }
    }
}
