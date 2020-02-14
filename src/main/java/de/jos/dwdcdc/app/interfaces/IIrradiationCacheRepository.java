package de.jos.dwdcdc.app.interfaces;

import java.util.Optional;

public interface IIrradiationCacheRepository {

    void delete(int id);

    Optional<IIrradiationCache> get(String key);

    void save(IIrradiationCache dbCache);
}
