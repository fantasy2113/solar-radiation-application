package org.josmer.application.handler;

import org.josmer.application.crawler.RadiationCrawler;
import org.josmer.application.enums.RadiationTypes;
import org.josmer.application.interfaces.IRadiationRepository;

public class InsertHandler implements Runnable {

    private final IRadiationRepository radiationRepository;

    public InsertHandler(IRadiationRepository radiationRepository) {
        this.radiationRepository = radiationRepository;
    }

    @Override
    public void run() {
        for (int year = 1991; year < 1992; year++) {
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
