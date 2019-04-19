package de.josmer.app.library.handler;


import de.josmer.app.library.crawler.RadiationCrawler;
import de.josmer.app.library.enums.RadiationTypes;
import de.josmer.app.repositories.RadiationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.time.LocalDate;

public final class InsertHandler extends AHandler {
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
        LOGGER.info(MessageFormat.format("try to insert: month: {0}, Year: {1} -> {2}", localDate.getMonth().getValue(), localDate.getYear(), radiationTypes)); // NOSONAR
        RadiationCrawler radiationCrawler = new RadiationCrawler(localDate.getMonth().getValue(), localDate.getYear(), radiationTypes);
        radiationCrawler.download();
        radiationCrawler.unzip();
        radiationCrawler.insert(new RadiationRepository());
        radiationCrawler.delete();
    }
}
