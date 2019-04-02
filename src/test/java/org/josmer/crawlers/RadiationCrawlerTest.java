package org.josmer.crawlers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RadiationCrawlerTest {
    private RadiationCrawler radiationCrawler;

    @BeforeEach
    void setUp() {
        radiationCrawler = new RadiationCrawler(1, 1991, "GLOBAL");
    }

    @AfterEach
    void tearDown() throws Exception {
        radiationCrawler.delete();
    }

    @Test
    public void testDownload() throws Exception {
        radiationCrawler.download();
        radiationCrawler.unzip();
        radiationCrawler.insert();
    }
}