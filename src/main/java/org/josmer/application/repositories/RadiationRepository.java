package org.josmer.application.repositories;


import org.josmer.application.entities.Radiation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RadiationRepository extends MongoRepository<Radiation, String> {


}