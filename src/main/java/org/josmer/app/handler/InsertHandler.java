package org.josmer.app.handler;

import org.josmer.app.core.IRadiationRepository;
import org.josmer.app.core.RadiationTypes;
import org.josmer.app.crawler.RadiationCrawler;

public class InsertHandler implements Runnable {

    private final IRadiationRepository radiationRepository;

    public InsertHandler(IRadiationRepository radiationRepository) {
        this.radiationRepository = radiationRepository;
    }

    @Override
    public void run() {
        for (int year = 2018; year < 2019; year++) {
            for (int month = 1; month < 13; month++) {
                RadiationCrawler monthlyRadiationCrawler = new RadiationCrawler(month, year, RadiationTypes.GLOBAL);
                monthlyRadiationCrawler.download();
                monthlyRadiationCrawler.unzip();
                monthlyRadiationCrawler.insert(radiationRepository);
                monthlyRadiationCrawler.delete();
            }
        }
    }
}
