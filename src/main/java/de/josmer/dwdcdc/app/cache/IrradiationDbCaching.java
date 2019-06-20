package de.josmer.dwdcdc.app.cache;

import de.josmer.dwdcdc.app.base.interfaces.IIrradiationCache;
import de.josmer.dwdcdc.app.base.interfaces.IIrradiationCacheRepository;
import de.josmer.dwdcdc.app.base.interfaces.IIrradiationCaching;
import de.josmer.dwdcdc.app.requests.IrrRequest;
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
    public Optional<IIrradiationCache> get(IrrRequest irrRequest) {
        Optional<IIrradiationCache> optionalDbCache = dbCacheRep.get(irrRequest.getKey());
        if (optionalDbCache.isEmpty()) {
            return Optional.empty();
        }
        if (isOldCache(optionalDbCache.get())) {
            dbCacheRep.delete(irrRequest.getId());
            return Optional.empty();
        }
        return optionalDbCache;
    }
}
