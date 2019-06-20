package de.josmer.dwdcdc.app.cache;

import de.josmer.dwdcdc.app.base.entities.cache.IrradiationCache;
import de.josmer.dwdcdc.app.base.interfaces.IIrradiationCacheRepository;
import de.josmer.dwdcdc.app.base.interfaces.IIrradiationCaching;
import de.josmer.dwdcdc.app.requests.IrrRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component("IrradiationCacheDbCaching")
public class IrradiationCacheDbCaching implements IIrradiationCaching {
    private final IIrradiationCacheRepository dbCacheRep;

    @Autowired
    public IrradiationCacheDbCaching(IIrradiationCacheRepository dbCacheRepository) {
        this.dbCacheRep = dbCacheRepository;
    }

    @Override
    public void add(IrradiationCache irradiationCache) {
        dbCacheRep.save(irradiationCache);
    }

    @Override
    public Optional<IrradiationCache> get(IrrRequest irrRequest) {
        LocalDateTime dtNow = LocalDateTime.now();
        Optional<IrradiationCache> optionalDbCache = dbCacheRep.get(irrRequest.getKey());
        if (optionalDbCache.isEmpty()) {
            return Optional.empty();
        }
        if (isOldCache(dtNow, optionalDbCache.get())) {
            dbCacheRep.delete(irrRequest.getId());
            return Optional.empty();
        }
        return optionalDbCache;
    }
}
