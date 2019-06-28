package de.josmer.dwdcdc.app.interfaces;

import java.time.LocalDate;
import java.util.Optional;

public interface IIrradiationCaching {

    void add(IIrradiationCache irradiationCache);

    Optional<IIrradiationCache> get(Identifiable irrRequest);

    default boolean isOldCache(IIrradiationCache irradiationCache) {
        LocalDate localDate = LocalDate.now();
        return irradiationCache.getCreated().getYear() == localDate.getYear()
                && irradiationCache.getCreated().getMonthValue() != localDate.getMonthValue();
    }
}
