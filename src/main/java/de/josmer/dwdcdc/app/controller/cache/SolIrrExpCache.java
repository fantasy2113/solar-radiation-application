package de.josmer.dwdcdc.app.controller.cache;

import de.josmer.dwdcdc.app.controller.requests.IrrRequest;
import de.josmer.dwdcdc.app.entities.SolIrrExp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SolIrrExpCache {

    protected static final Logger LOGGER = LoggerFactory.getLogger(SolIrrExpCache.class.getName());

    private static final int LIMIT = 1000;

    private final ConcurrentHashMap<String, LinkedList<SolIrrExp>> computedSolIrrExps;

    public SolIrrExpCache() {
        this.computedSolIrrExps = new ConcurrentHashMap<>();
    }

    public void add(final IrrRequest irrRequest, LinkedList<SolIrrExp> solIrrExps) {
        try {
            clear();
            computedSolIrrExps.putIfAbsent(irrRequest.toString(), solIrrExps);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }

    public Optional<LinkedList<SolIrrExp>> get(final IrrRequest irrRequest) {
        return Optional.ofNullable(computedSolIrrExps.get(irrRequest.toString()));
    }

    private void clear() {
        if (computedSolIrrExps.size() > LIMIT) {
            computedSolIrrExps.clear();
        }
    }
}
