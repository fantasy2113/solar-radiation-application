package de.josmer.dwdcdc.app.base.interfaces;

import de.josmer.dwdcdc.app.base.entities.cache.IrradiationCache;

import java.util.Optional;

public interface IIrradiationCacheRepository {

    Optional<IrradiationCache> get(String key);

    void save(IrradiationCache dbCache);

    void delete(int id);
}
