package de.josmer.application.libraries.handler;

import de.josmer.application.libraries.crawler.RadiationCrawler;
import de.josmer.application.libraries.enums.RadTypes;
import de.josmer.application.libraries.utils.FileReader;
import de.josmer.application.repositories.SolRadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.time.LocalDate;

public final class InsertHandler extends AHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(InsertHandler.class.getName());
    private final RadTypes radTypes;

    public InsertHandler(RadTypes radTypes) {
        this.radTypes = radTypes;
    }

    @Override
    public void run() {
        LocalDate localDate = LocalDate.now();
        if (localDate.getDayOfMonth() < 15) {
            localDate = localDate.minusMonths(2);
        } else {
            localDate = localDate.minusMonths(1);
        }
        LOGGER.info(MessageFormat.format("try to insert: month: {0}, Year: {1} -> {2}", localDate.getMonth().getValue(), localDate.getYear(), radTypes)); // NOSONAR
        RadiationCrawler radiationCrawler = new RadiationCrawler(localDate.getMonth().getValue(), localDate.getYear(), radTypes);
        radiationCrawler.download();
        radiationCrawler.unzip();
        radiationCrawler.insert(new SolRadRepository(), new FileReader());
        radiationCrawler.delete();
        System.gc();
    }
}
