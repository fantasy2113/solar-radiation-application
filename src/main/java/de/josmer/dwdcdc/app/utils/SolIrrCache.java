package de.josmer.dwdcdc.app.utils;

import de.josmer.dwdcdc.utils.solar.ComputedYear;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SolIrrCache {

    private static final int LIMIT = 100000;

    private final ConcurrentHashMap<String, ComputedYear> computedYears;

    public SolIrrCache() {
        this.computedYears = new ConcurrentHashMap<>();
    }

    public boolean contains(double lon, double lat, int ae, int ye, int year) {
        return computedYears.contains(getKey(lon, lat, ae, ye, year));
    }

    public Optional<ComputedYear> get(double lon, double lat, int ae, int ye, int year) {
        return Optional.ofNullable(computedYears.get(getKey(lon, lat, ae, ye, year)));
    }

    private String getKey(double lon, double lat, int ae, int ye, int year) {
        return String.valueOf(lon) + lat + ae + ye + year;
    }
}
