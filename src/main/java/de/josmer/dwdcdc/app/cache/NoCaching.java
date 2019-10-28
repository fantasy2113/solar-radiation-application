package de.josmer.dwdcdc.app.cache;

import java.util.Optional;

import org.springframework.stereotype.Component;

import de.josmer.dwdcdc.app.interfaces.IIrradiationCache;
import de.josmer.dwdcdc.app.interfaces.IIrradiationCaching;
import de.josmer.dwdcdc.app.interfaces.Identifiable;

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
