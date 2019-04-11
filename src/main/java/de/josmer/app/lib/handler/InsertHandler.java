package de.josmer.app.lib.handler;


import de.josmer.app.lib.crawler.RadiationCrawler;
import de.josmer.app.lib.enums.RadiationTypes;
import de.josmer.app.repositories.RadiationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public final class InsertHandler extends Handler {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsertHandler.class.getName());
    private final RadiationTypes radiationTypes;

    public InsertHandler(RadiationTypes radiationTypes) {
        this.radiationTypes = radiationTypes;
    }


    @Override
    public void run() {
        LocalDate localDate = LocalDate.now();
        if (localDate.getDayOfMonth() < 15) {
            localDate = localDate.minusMonths(2);
        } else {
            localDate = localDate.minusMonths(1);
        }
        LOGGER.info("try to insert: month: " + localDate.getMonth().getValue() + ", Year: " + localDate.getYear() + " -> " + radiationTypes);
        RadiationCrawler radiationCrawler = new RadiationCrawler(localDate.getMonth().getValue(), localDate.getYear(), radiationTypes);
        radiationCrawler.download();
        radiationCrawler.unzip();
        radiationCrawler.insert(new RadiationRepository());
        radiationCrawler.delete();
    }
}
