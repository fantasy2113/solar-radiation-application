package org.josmer.app.lib.handler;

import org.josmer.app.lib.core.IRadiationRepository;
import org.josmer.app.lib.core.RadiationTypes;
import org.josmer.app.lib.crawler.RadiationCrawler;

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
