package org.josmer.application.utils;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DwdCrawlerTest {
    private DwdCrawler dwdCrawler;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        dwdCrawler.delete();
    }

    @Test
    public void testDownload() {
        dwdCrawler = new DwdCrawler("grids_germany_monthly_radiation_global_199101.zip");
        dwdCrawler.download();
        dwdCrawler.unzip();
    }
}