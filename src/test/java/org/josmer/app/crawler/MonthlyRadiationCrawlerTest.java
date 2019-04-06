package org.josmer.app.crawler;

import org.josmer.app.core.RadiationTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MonthlyRadiationCrawlerTest {
    private MonthlyRadiationCrawler monthlyRadiationCrawler;

    @BeforeEach
    void setUp() {
        monthlyRadiationCrawler = new MonthlyRadiationCrawler(1, 1991, RadiationTypes.GLOBAL);
    }

    @AfterEach
    void tearDown() {
        monthlyRadiationCrawler.delete();
    }

    @Test
    public void testDownload() {
        monthlyRadiationCrawler.download();
        monthlyRadiationCrawler.unzip();
    }
}