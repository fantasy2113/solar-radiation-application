package org.josmer.controller;

import org.josmer.interfaces.IRadiationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/radiation")
public class RadiationController {
    @Autowired
    IRadiationRepository radiationRepository;

    @RequestMapping("/")
    public String index() {
        return "";
    }

}
