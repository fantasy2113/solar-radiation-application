package org.josmer.application.utils;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DwdCrawlerTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testDownload(){

        DwdCrawler dwdCrawler = new DwdCrawler("grids_germany_monthly_radiation_global_199101.zip");
        dwdCrawler.download();
        dwdCrawler.unzip();
    }
}