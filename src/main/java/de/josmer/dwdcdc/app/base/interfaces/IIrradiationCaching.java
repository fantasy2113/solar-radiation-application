package de.josmer.dwdcdc.app.base.interfaces;

import de.josmer.dwdcdc.app.requests.IrrRequest;

import java.time.LocalDateTime;
import java.util.Optional;

public interface IIrradiationCaching {
    void add(IIrradiationCache irradiationCache);

    Optional<IIrradiationCache> get(IrrRequest irrRequest);

    default boolean isOldCache(LocalDateTime dtNow, IIrradiationCache irradiationCache) {
        return irradiationCache.getCreated().getYear() == dtNow.getYear()
                && irradiationCache.getCreated().getMonthValue() != dtNow.getMonthValue();
    }
}
