package de.josmer.dwdcdc.app.cache;

import de.josmer.dwdcdc.app.base.entities.SolIrrExp;
import de.josmer.dwdcdc.app.base.entities.cache.DbCache;
import de.josmer.dwdcdc.app.base.interfaces.IDbCacheRepository;
import de.josmer.dwdcdc.app.base.interfaces.IJsonb;
import de.josmer.dwdcdc.app.base.interfaces.ISolIrrExpDbCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Optional;

@Component("SolIrrExpDbCache")
public class SolIrrExpDbCache implements ISolIrrExpDbCache {
    private final IDbCacheRepository<IJsonb> dbCacheRepository;

    @Autowired
    public SolIrrExpDbCache(IDbCacheRepository dbCacheRepository) {
        this.dbCacheRepository = dbCacheRepository;
    }

    @Override
    public void add(IJsonb irrRequest, LinkedList<SolIrrExp> solIrrExps) {
        dbCacheRepository.save(irrRequest, solIrrExps);
    }

    @Override
    public Optional<LinkedList<SolIrrExp>> get(IJsonb irrRequest) {
        return dbCacheRepository.find(irrRequest).map(DbCache::getMonths);
    }
}
