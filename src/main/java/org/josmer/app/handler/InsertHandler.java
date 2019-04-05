package org.josmer.app.handler;

import org.josmer.app.core.RadiationTypes;
import org.josmer.app.crawler.RadiationCrawler;

public class InsertHandler implements Runnable {
    @Override
    public void run() {
        for (int year = 2018; year < 2019; year++) {
            for (int month = 1; month < 13; month++) {
                RadiationCrawler radiationCrawler = new RadiationCrawler(month, year, RadiationTypes.GLOBAL);
                radiationCrawler.download();
                radiationCrawler.unzip();
                radiationCrawler.insert();
                radiationCrawler.delete();
            }
        }
    }
}
