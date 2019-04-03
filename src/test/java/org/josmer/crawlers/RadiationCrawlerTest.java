package org.josmer.crawlers;

import org.josmer.utils.RadiationTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RadiationCrawlerTest {
    private RadiationCrawler radiationCrawler;

    @BeforeEach
    void setUp() {
        radiationCrawler = new RadiationCrawler(1, 1991, RadiationTypes.GLOBAL);
    }

    @AfterEach
    void tearDown() {
        radiationCrawler.delete();
    }

    @Test
    public void testDownload() {
        radiationCrawler.download();
        radiationCrawler.unzip();
    }
}