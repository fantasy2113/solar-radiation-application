package de.josmer.dwdcdc.app.cache;

import de.josmer.dwdcdc.app.base.entities.SolIrrExp;
import de.josmer.dwdcdc.app.base.entities.cache.DbCache;
import de.josmer.dwdcdc.app.base.interfaces.IDbCacheRepository;
import de.josmer.dwdcdc.app.base.interfaces.IJsonb;
import de.josmer.dwdcdc.app.base.interfaces.ISolIrrExpDbCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Optional;

@Component("SolIrrExpDbCache")
public class SolIrrExpDbCache implements ISolIrrExpDbCache {
    private final IDbCacheRepository<IJsonb> dbCacheRep;

    @Autowired
    public SolIrrExpDbCache(IDbCacheRepository dbCacheRepository) {
        this.dbCacheRep = dbCacheRepository;
    }

    @Override
    public void add(IJsonb item, LinkedList<SolIrrExp> solIrrExps) {
        dbCacheRep.save(item, solIrrExps);
    }

    @Override
    public Optional<LinkedList<SolIrrExp>> get(IJsonb item) {
        LocalDateTime dtNow = LocalDateTime.now();
        return getCache(item, dtNow);
    }

    private Optional<LinkedList<SolIrrExp>> getCache(IJsonb item, LocalDateTime dtNow) {
        Optional<LinkedList<SolIrrExp>> optionalMonths = dbCacheRep.find(item).map(DbCache::getMonths);
        if (optionalMonths.isEmpty() || dtNow.getYear() == item.getYear()) {
            return optionalMonths;
        }
        if (dtNow.getDayOfMonth() < 15) {
            return optionalMonths;
        }
        dbCacheRep.delete(item);
        return Optional.empty();
    }
}
