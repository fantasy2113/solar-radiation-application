package de.josmer.dwdcdc.app.cache;

import de.josmer.dwdcdc.app.base.entities.cache.DbCache;
import de.josmer.dwdcdc.app.base.interfaces.ISolIrrExpCache;
import de.josmer.dwdcdc.app.requests.IrrRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component("SolIrrExpRamCache")
public class SolIrrExpRamCache implements ISolIrrExpCache {
    private static final int LIMIT = 10000;
    private final ConcurrentHashMap<String, DbCache> computedSolIrrExps;
    private final ISolIrrExpCache solIrrExpDbCache;

    @Autowired
    public SolIrrExpRamCache(ISolIrrExpCache solIrrExpDbCache) {
        this.computedSolIrrExps = new ConcurrentHashMap<>();
        this.solIrrExpDbCache = solIrrExpDbCache;
    }

    @Override
    public void add(DbCache dbCache) {
        addCache(dbCache);
    }

    @Override
    public Optional<DbCache> get(IrrRequest irrRequest) {
        Optional<DbCache> optionalRamCache = getCache(irrRequest);
        if (optionalRamCache.isPresent()) {
            return optionalRamCache;
        }

        Optional<DbCache> optionalDbCache = solIrrExpDbCache.get(irrRequest);
        if (optionalDbCache.isPresent()) {
            addRamCache(optionalDbCache.get());
            return optionalDbCache;
        }

        return Optional.empty();
    }

    private void addRamCache(DbCache dbCache) {
        cleanRamCache();
        computedSolIrrExps.putIfAbsent(dbCache.getKey(), dbCache);
    }

    private void addCache(DbCache dbCache) {
        addRamCache(dbCache);
        solIrrExpDbCache.add(dbCache);
    }

    private Optional<DbCache> getCache(IrrRequest irrRequest) {
        return Optional.ofNullable(computedSolIrrExps.get(irrRequest.getKey()));
    }

    private void cleanRamCache() {
        if (computedSolIrrExps.size() >= LIMIT) {
            computedSolIrrExps.remove(computedSolIrrExps.keys().nextElement());
        }
    }
}
