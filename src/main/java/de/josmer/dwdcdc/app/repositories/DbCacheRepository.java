package de.josmer.dwdcdc.app.repositories;

import de.josmer.dwdcdc.app.entities.DbCache;
import de.josmer.dwdcdc.app.entities.SolIrrExp;
import de.josmer.dwdcdc.app.interfaces.IDbCacheRepository;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Optional;

@Component
public class DbCacheRepository implements IDbCacheRepository {
    @Override
    public Optional<LinkedList<SolIrrExp>> find(final String key) {
        return Optional.empty();
    }

    @Override
    public void save(DbCache dbCache) {

    }
}
