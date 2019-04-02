package org.josmer.repositories;


import org.josmer.entities.Radiation;
import org.josmer.interfaces.IRadiationConnector;
import org.josmer.interfaces.IRadiationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class RadiationRepository implements IRadiationRepository {
    @Autowired
    IRadiationConnector connector;

    @Override
    public Optional<Radiation> get(long id) {
        return Optional.empty();
    }

    @Override
    public List<Radiation> getAll() {
        return new ArrayList<>();
    }

    @Override
    public void save(Radiation radiation) {

    }

    @Override
    public void update(Radiation radiation) {

    }

    @Override
    public void delete(Radiation radiation) {

    }
}