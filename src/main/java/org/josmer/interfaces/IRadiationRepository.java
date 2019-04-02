package org.josmer.interfaces;

import org.josmer.entities.Radiation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IRadiationRepository extends IRepository<Radiation> {
    int saveAll(List<Radiation> radiations);
}
