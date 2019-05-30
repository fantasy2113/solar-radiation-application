package de.josmer.dwdcdc.app.utils;

import de.josmer.dwdcdc.app.entities.SolIrrExp;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SolIrrExpCache {

    private static final int LIMIT = 100000;

    private final ConcurrentHashMap<String, LinkedList<SolIrrExp>> computedSolIrrExps;

    public SolIrrExpCache() {
        this.computedSolIrrExps = new ConcurrentHashMap<>();
    }

    public boolean contains(double lon, double lat, int ae, int ye, int year) {
        return computedSolIrrExps.contains(getKey(lon, lat, ae, ye, year));
    }

    public Optional<LinkedList<SolIrrExp>> get(double lon, double lat, int ae, int ye, int year) {
        return Optional.ofNullable(computedSolIrrExps.get(getKey(lon, lat, ae, ye, year)));
    }

    private String getKey(double lon, double lat, int ae, int ye, int year) {
        return String.valueOf(lon) + lat + ae + ye + year;
    }
}
