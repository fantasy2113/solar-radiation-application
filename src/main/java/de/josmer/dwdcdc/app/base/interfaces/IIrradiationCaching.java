package de.josmer.dwdcdc.app.base.interfaces;

import java.time.LocalDateTime;
import java.util.Optional;

public interface IIrradiationCaching {
    void add(IIrradiationCache irradiationCache);

    Optional<IIrradiationCache> get(Identifiable irrRequest);

    default boolean isOldCache(IIrradiationCache irradiationCache) {
        LocalDateTime dtNow = LocalDateTime.now();
        return irradiationCache.getCreated().getYear() == dtNow.getYear()
                && irradiationCache.getCreated().getMonthValue() != dtNow.getMonthValue();
    }
}
