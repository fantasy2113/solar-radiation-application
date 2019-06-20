package de.josmer.dwdcdc.app.cache;

import de.josmer.dwdcdc.app.base.entities.cache.DbCache;
import de.josmer.dwdcdc.app.base.interfaces.IDbCacheRepository;
import de.josmer.dwdcdc.app.base.interfaces.ISolIrrExpCache;
import de.josmer.dwdcdc.app.requests.IrrRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component("SolIrrExpDbCache")
public class SolIrrExpDbCache implements ISolIrrExpCache {
    private final IDbCacheRepository dbCacheRep;

    @Autowired
    public SolIrrExpDbCache(IDbCacheRepository dbCacheRepository) {
        this.dbCacheRep = dbCacheRepository;
    }

    @Override
    public void add(DbCache dbCache) {
        dbCacheRep.save(dbCache);
    }

    @Override
    public Optional<DbCache> get(IrrRequest irrRequest) {
        LocalDateTime dtNow = LocalDateTime.now();
        Optional<DbCache> optionalDbCache = dbCacheRep.find(irrRequest.getKey());
        if (optionalDbCache.isEmpty()) {
            return Optional.empty();
        }
        if (isOldCache(dtNow, optionalDbCache.get())) {
            dbCacheRep.delete(irrRequest.getKey());
            return Optional.empty();
        }
        return optionalDbCache;
    }

    private boolean isOldCache(LocalDateTime dtNow, DbCache dbCache) {
        return dbCache.getCreated().getYear() == dtNow.getYear()
                && dbCache.getCreated().getMonthValue() != dtNow.getMonthValue();
    }
}
