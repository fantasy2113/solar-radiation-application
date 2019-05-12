package de.josmer.app.library.handler;

import de.josmer.app.library.crawler.RadiationCrawler;
import de.josmer.app.library.enums.RadTypes;
import de.josmer.app.model.repositories.SolRadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class InsertHandler extends AHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(InsertHandler.class.getName());
    private final RadTypes radTypes;

    public InsertHandler(RadTypes radTypes) {
        this.radTypes = radTypes;
    }

    @Override
    public void run() {
        /*LocalDate localDate = LocalDate.now();
        if (localDate.getDayOfMonth() < 15) {
            localDate = localDate.minusMonths(2);
        } else {
            localDate = localDate.minusMonths(1);
        }
        LOGGER.info(MessageFormat.format("try to insert: month: {0}, Year: {1} -> {2}", localDate.getMonth().getValue(), localDate.getYear(), radTypes)); // NOSONAR
        RadiationCrawler radiationCrawler = new RadiationCrawler(localDate.getMonth().getValue(), localDate.getYear(), radTypes);
        radiationCrawler.download();
        radiationCrawler.unzip();
        radiationCrawler.insert(new SolRadRepository());
        radiationCrawler.delete();*/
        for (int year = 1991; year < 2020; year++) {
            for (int month = 1; month < 13; month++) {
                LOGGER.info(">>> Month: " + month + ", Year: " + year);
                RadiationCrawler radiationCrawler = new RadiationCrawler(month, year, radTypes);
                radiationCrawler.download();
                radiationCrawler.unzip();
                radiationCrawler.insert(new SolRadRepository());
                radiationCrawler.delete();
            }
        }

    }
}
