package org.josmer.repositories;


import org.josmer.entities.Radiation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RadiationRepository extends MongoRepository<Radiation, String> {


}