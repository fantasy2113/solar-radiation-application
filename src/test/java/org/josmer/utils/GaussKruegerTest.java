package org.josmer.utils;

import org.josmer.app.logic.utils.GaussKrueger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GaussKruegerTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void calculate() {
        GaussKrueger gaussKrueger = new GaussKrueger(9, 53);
        gaussKrueger.calculate();
        double rechtswert = gaussKrueger.getRechtswert();
        double Hochwert = gaussKrueger.getHochwert();
    }
}