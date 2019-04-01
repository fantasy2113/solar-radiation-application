package org.josmer.controller;

import org.josmer.entities.Radiation;
import org.josmer.repositories.RadiationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/radiation")
public class RadiationController {
    @Autowired
    private RadiationRepository repository;

    @RequestMapping(path = "all")
    public List<Radiation> all() {
        return repository.findAll();
    }
}
