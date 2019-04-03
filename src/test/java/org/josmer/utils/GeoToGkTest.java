package org.josmer.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GeoToGkTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void calculate() {
        GeoToGk geoToGk = new GeoToGk(9, 53);
        geoToGk.calculate();
        double rechtswert = geoToGk.getRechtswert();
        double Hochwert = geoToGk.getHochwert();
    }
}