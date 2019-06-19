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
        cleaning();
        solIrrExpDbCache.add(irrRequest, solIrrExps);
        computedSolIrrExps.putIfAbsent(irrRequest.getKey(), solIrrExps);
    }

    @Override
    public Optional<LinkedList<SolIrrExp>> get(final IrrRequest irrRequest) {
        Optional<LinkedList<SolIrrExp>> optionalCache = getCache(irrRequest);
        if (optionalCache.isPresent()) {
            return optionalCache;
        }

        Optional<LinkedList<SolIrrExp>> solIrrExps = solIrrExpDbCache.get(irrRequest);
        if (solIrrExps.isPresent()) {
            add(irrRequest, solIrrExps.get());
            return solIrrExps;
        }

        return Optional.empty();
    }

    private Optional<LinkedList<SolIrrExp>> getCache(IrrRequest irrRequest) {
        return Optional.ofNullable(computedSolIrrExps.get(irrRequest.getKey()));
    }

    private void cleaning() {
        if (computedSolIrrExps.size() >= LIMIT) {
            computedSolIrrExps.remove(computedSolIrrExps.keys().nextElement());
        }
    }
}
