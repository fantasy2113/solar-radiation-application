package org.josmer.app.handler;

import org.josmer.app.core.RadiationTypes;
import org.josmer.app.crawler.MonthlyRadiationCrawler;

public class InsertHandler implements Runnable {
    @Override
    public void run() {
        for (int year = 2018; year < 2019; year++) {
            for (int month = 1; month < 13; month++) {
                MonthlyRadiationCrawler monthlyRadiationCrawler = new MonthlyRadiationCrawler(month, year, RadiationTypes.GLOBAL);
                monthlyRadiationCrawler.download();
                monthlyRadiationCrawler.unzip();
                monthlyRadiationCrawler.insert();
                monthlyRadiationCrawler.delete();
            }
        }
    }
}
