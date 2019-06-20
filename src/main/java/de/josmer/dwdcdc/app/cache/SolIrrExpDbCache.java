package de.josmer.dwdcdc.app.cache;

import de.josmer.dwdcdc.app.base.entities.SolIrrExp;
import de.josmer.dwdcdc.app.base.entities.cache.DbCache;
import de.josmer.dwdcdc.app.base.interfaces.IDbCacheRepository;
import de.josmer.dwdcdc.app.base.interfaces.ISolIrrExpDbCache;
import de.josmer.dwdcdc.app.requests.IrrRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Optional;

@Component("SolIrrExpDbCache")
public class SolIrrExpDbCache implements ISolIrrExpDbCache {
    private final IDbCacheRepository<DbCache> dbCacheRepository;

    @Autowired
    public SolIrrExpDbCache(IDbCacheRepository dbCacheRepository) {
        this.dbCacheRepository = dbCacheRepository;
    }

    @Override
    public void add(IrrRequest irrRequest, LinkedList<SolIrrExp> solIrrExps) {
        dbCacheRepository.save(new DbCache(irrRequest.getKey(), solIrrExps));
    }

    @Override
    public Optional<LinkedList<SolIrrExp>> get(IrrRequest irrRequest) {
        return dbCacheRepository.find(irrRequest.getKey()).map(DbCache::getMonths);
    }
}
