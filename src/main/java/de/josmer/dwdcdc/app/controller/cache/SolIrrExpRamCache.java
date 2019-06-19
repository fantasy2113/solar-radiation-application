package de.josmer.dwdcdc.app.controller.cache;

import de.josmer.dwdcdc.app.controller.requests.IrrRequest;
import de.josmer.dwdcdc.app.entities.SolIrrExp;
import de.josmer.dwdcdc.app.interfaces.ISolIrrExpCache;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component("SolIrrExpRamCache")
public class SolIrrExpRamCache implements ISolIrrExpCache<IrrRequest> {
    private static final int LIMIT = 10000;
    private final ConcurrentHashMap<String, LinkedList<SolIrrExp>> computedSolIrrExps;

    public SolIrrExpRamCache() {
        this.computedSolIrrExps = new ConcurrentHashMap<>();
    }

    @Override
    public void add(final IrrRequest irrRequest, final LinkedList<SolIrrExp> solIrrExps) {
        cleaning();
        computedSolIrrExps.putIfAbsent(irrRequest.getKey(), solIrrExps);
    }

    @Override
    public Optional<LinkedList<SolIrrExp>> get(final IrrRequest irrRequest) {
        return Optional.ofNullable(computedSolIrrExps.get(irrRequest.getKey()));
    }

    private void cleaning() {
        if (computedSolIrrExps.size() >= LIMIT) {
            computedSolIrrExps.remove(computedSolIrrExps.keys().nextElement());
        }
    }
}
