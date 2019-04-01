package org.josmer.application.controller;

import org.josmer.application.entities.Radiation;
import org.josmer.application.repositories.RadiationRepository;
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
