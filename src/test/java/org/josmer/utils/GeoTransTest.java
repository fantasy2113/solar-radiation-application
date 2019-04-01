package org.josmer.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GeoTransTest {
    private GeoTrans geoTrans;

    @BeforeEach
    void setUp() {
        geoTrans = new GeoTrans();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void wgs84ToPotsdamDatum() {

    }

    @Test
    void potsdamDatumToWgs84() {
    }

    @Test
    void geoToGk() {
        geoTrans.geoToGk(9, 53);
        double resultRw = geoTrans.getResultRw();
        double resultHw = geoTrans.getResultHw();
    }
}