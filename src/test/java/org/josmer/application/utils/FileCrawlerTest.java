package org.josmer.application.utils;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileCrawlerTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testDownload(){

        FileCrawler fileCrawler = new FileCrawler("grids_germany_monthly_radiation_global_199101.zip",
                "https://opendata.dwd.de/climate_environment/CDC/grids_germany/monthly/radiation_global/");
        fileCrawler.download();
    }
}