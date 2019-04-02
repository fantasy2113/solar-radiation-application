package org.josmer.controller;

import org.josmer.entities.Radiation;
import org.josmer.interfaces.IRadiationRepository;
import org.josmer.security.Key;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/radiation")
public final class RadiationController {
    @Autowired
    IRadiationRepository radiationRepository;

    @ResponseBody
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Radiation get(@CookieValue("key") final String key, @RequestParam(name = "id") long id) {
        if (!Key.check(key)) {
            return null;
        }
        Optional<Radiation> optionalRadiation = radiationRepository.get(id);
        return optionalRadiation.isPresent() ? optionalRadiation.get() : new Radiation();
    }

}
