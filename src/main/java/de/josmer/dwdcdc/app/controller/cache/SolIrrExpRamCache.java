package de.josmer.dwdcdc.app.controller.cache;

import de.josmer.dwdcdc.app.controller.requests.IrrRequest;
import de.josmer.dwdcdc.app.entities.SolIrrExp;
import de.josmer.dwdcdc.app.interfaces.ISolIrrExpCache;
import de.josmer.dwdcdc.app.interfaces.ISolIrrExpDbCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component("SolIrrExpRamCache")
public class SolIrrExpRamCache implements ISolIrrExpCache<IrrRequest> {
    private static final int LIMIT = 10000;
    private final ConcurrentHashMap<String, LinkedList<SolIrrExp>> computedSolIrrExps;
    private final ISolIrrExpDbCache solIrrExpDbCache;

    @Autowired
    public SolIrrExpRamCache(ISolIrrExpDbCache solIrrExpDbCache) {
        this.computedSolIrrExps = new ConcurrentHashMap<>();
        this.solIrrExpDbCache = solIrrExpDbCache;
    }

    @Override
    public void add(final IrrRequest irrRequest, final LinkedList<SolIrrExp> solIrrExps) {
        addCache(irrRequest, solIrrExps);
    }

    @Override
    public Optional<LinkedList<SolIrrExp>> get(final IrrRequest irrRequest) {
        Optional<LinkedList<SolIrrExp>> optionalRamCache = getCache(irrRequest);
        if (optionalRamCache.isPresent()) {
            return optionalRamCache;
        }

        Optional<LinkedList<SolIrrExp>> optionalDbCache = solIrrExpDbCache.get(irrRequest);
        if (optionalDbCache.isPresent()) {
            addRamCache(irrRequest, optionalDbCache.get());
            return optionalDbCache;
        }

        return Optional.empty();
    }

    private void addRamCache(IrrRequest irrRequest, LinkedList<SolIrrExp> solIrrExps) {
        cleanRamCache();
        computedSolIrrExps.putIfAbsent(irrRequest.getKey(), solIrrExps);
    }

    private void addCache(IrrRequest irrRequest, LinkedList<SolIrrExp> solIrrExps) {
        addRamCache(irrRequest, solIrrExps);
        solIrrExpDbCache.add(irrRequest, solIrrExps);
    }

    private Optional<LinkedList<SolIrrExp>> getCache(IrrRequest irrRequest) {
        return Optional.ofNullable(computedSolIrrExps.get(irrRequest.getKey()));
    }

    private void cleanRamCache() {
        if (computedSolIrrExps.size() >= LIMIT) {
            computedSolIrrExps.remove(computedSolIrrExps.keys().nextElement());
        }
    }
}
