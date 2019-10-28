package de.josmer.dwdcdc.app.interfaces;

import java.util.Optional;

public interface IIrradiationCacheRepository {

	Optional<IIrradiationCache> get(String key);

	void save(IIrradiationCache dbCache);

	void delete(int id);
}
