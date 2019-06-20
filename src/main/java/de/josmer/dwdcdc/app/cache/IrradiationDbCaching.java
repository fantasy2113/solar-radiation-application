package de.josmer.dwdcdc.app.cache;

import de.josmer.dwdcdc.app.base.interfaces.IIrradiationCache;
import de.josmer.dwdcdc.app.base.interfaces.IIrradiationCacheRepository;
import de.josmer.dwdcdc.app.base.interfaces.IIrradiationCaching;
import de.josmer.dwdcdc.app.base.interfaces.Identifiable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("IrradiationDbCaching")
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
        Optional<IIrradiationCache> optionalDbCache = dbCacheRep.get(identifiable.getKey());
        if (optionalDbCache.isEmpty()) {
            return Optional.empty();
        }
        if (isOldCache(optionalDbCache.get())) {
            dbCacheRep.delete(identifiable.getId());
            return Optional.empty();
        }
        return optionalDbCache;
    }
}
