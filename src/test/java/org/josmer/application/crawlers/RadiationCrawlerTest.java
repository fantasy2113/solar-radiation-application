package org.josmer.application.crawlers;


import org.josmer.application.interfaces.ICrawler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RadiationCrawlerTest {
    private ICrawler crawler;

    @BeforeEach
    void setUp() {
        crawler = new RadiationCrawler();
    }

    @AfterEach
    void tearDown() throws Exception {
        crawler.delete();
    }

    @Test
    public void testDownload() throws Exception {
        crawler.download("199101");
        crawler.unzip();
    }
}