package de.josmer.dwdcdc.app.cache;

import de.josmer.dwdcdc.app.interfaces.IIrradiationCache;
import de.josmer.dwdcdc.app.interfaces.IIrradiationCacheRepository;
import de.josmer.dwdcdc.app.interfaces.IIrradiationCaching;
import de.josmer.dwdcdc.app.interfaces.Identifiable;
import de.josmer.dwdcdc.app.spring.BeanNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component(BeanNames.IRRADIATION_DB_CACHING)
public class IrradiationDbCaching implements IIrradiationCaching {

    private final IIrradiationCacheRepository dbCacheRep;

    @Autowired
    public IrradiationDbCaching(IIrradiationCacheRepository dbCacheRepository) {
        this.dbCacheRep = dbCacheRepository;
    }

    @Override
    public void add(IIrradiationCache irradiationCache) {
        dbCacheRep.save(irradiationCache);
    }

    @Override
    public Optional<IIrradiationCache> get(Identifiable identifiable) {
        Optional<IIrradiationCache> optionalIIrradiationCache = dbCacheRep.get(identifiable.getKey());
        if (optionalIIrradiationCache.isEmpty()) {
            return Optional.empty();
        }
        if (isOldCache(optionalIIrradiationCache.get())) {
            dbCacheRep.delete(identifiable.getId());
            return Optional.empty();
        }
        return optionalIIrradiationCache;
    }
}
