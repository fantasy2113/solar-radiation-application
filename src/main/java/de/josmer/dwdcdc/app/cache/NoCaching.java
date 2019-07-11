package de.josmer.dwdcdc.app.cache;

import de.josmer.dwdcdc.app.interfaces.IIrradiationCache;
import de.josmer.dwdcdc.app.interfaces.IIrradiationCaching;
import de.josmer.dwdcdc.app.interfaces.Identifiable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("NoCaching")
public class NoCaching implements IIrradiationCaching {
    @Override
    public void add(IIrradiationCache irradiationCache) {
        // do nothing
    }

    @Override
    public Optional<IIrradiationCache> get(Identifiable irrRequest) {
        return Optional.empty();
    }
}
