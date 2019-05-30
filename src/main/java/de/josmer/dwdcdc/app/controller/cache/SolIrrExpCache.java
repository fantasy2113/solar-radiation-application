package de.josmer.dwdcdc.app.controller.cache;

import de.josmer.dwdcdc.app.controller.requests.IrrRequest;
import de.josmer.dwdcdc.app.entities.SolIrrExp;
import de.josmer.dwdcdc.app.interfaces.ISolIrrExpCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SolIrrExpCache implements ISolIrrExpCache {
    protected static final Logger LOGGER = LoggerFactory.getLogger(SolIrrExpCache.class.getName());
    private static final int LIMIT = 1000;
    private final ConcurrentHashMap<String, LinkedList<SolIrrExp>> computedSolIrrExps;

    public SolIrrExpCache() {
        this.computedSolIrrExps = new ConcurrentHashMap<>();
    }

    @Override
    public void add(final IrrRequest irrRequest, final LinkedList<SolIrrExp> solIrrExps) {
        try {
            clear();
            final String key = irrRequest.toString();
            computedSolIrrExps.putIfAbsent(key, solIrrExps);
            LOGGER.info("cache: " + key);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }

    @Override
    public Optional<LinkedList<SolIrrExp>> get(final IrrRequest irrRequest) {
        try {
            return Optional.ofNullable(computedSolIrrExps.get(irrRequest.toString()));
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return Optional.empty();
    }

    private void clear() {
        if (computedSolIrrExps.size() > LIMIT) {
            computedSolIrrExps.clear();
            LOGGER.info("clear");
        }
    }
}
