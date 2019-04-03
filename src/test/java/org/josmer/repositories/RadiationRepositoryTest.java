package org.josmer.repositories;

import org.josmer.entities.Radiation;
import org.josmer.utils.RadiationTypes;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RadiationRepositoryTest {

    @Test
    void find1() {
        RadiationRepository radiationRepository = new RadiationRepository();
        List<Radiation> radiations = radiationRepository.find(199101, 199112, RadiationTypes.GLOBAL.name(), 9.4, 53.5);
        assertEquals(12, radiations.size());
    }

    @Test
    void find2() {
        RadiationRepository radiationRepository = new RadiationRepository();
        List<Radiation> radiations = radiationRepository.find(199101, 199111, RadiationTypes.GLOBAL.name(), 9.4, 53.5);
        assertEquals(11, radiations.size());
    }

    @Test
    void find3() {
        RadiationRepository radiationRepository = new RadiationRepository();
        List<Radiation> radiations = radiationRepository.find(199101, 199211, RadiationTypes.GLOBAL.name(), 9.4, 53.5);
        assertEquals(23, radiations.size());
    }

    @Test
    void save() {
    }

    @Test
    void isConnected() {
    }
}