package org.josmer.application;

import org.josmer.application.repositories.RadiationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {
    @Autowired
    private RadiationRepository repository;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

}
