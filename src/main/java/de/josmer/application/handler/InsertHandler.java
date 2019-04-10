package de.josmer.application.handler;

import de.josmer.application.crawler.RadiationCrawler;
import de.josmer.application.enums.RadiationTypes;
import de.josmer.application.interfaces.IRadiationRepository;
import de.josmer.application.repositories.RadiationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class InsertHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsertHandler.class.getName());
    @Autowired
    private IRadiationRepository radiationRepository;

    @Override
    public void run() {
        LOGGER.info("global inserting...");
        insertData(RadiationTypes.GLOBAL);
        LOGGER.info("direct inserting...");
        insertData(RadiationTypes.DIRECT);
        LOGGER.info("diffuse inserting...");
        insertData(RadiationTypes.DIFFUSE);
    }

    public void insertData(RadiationTypes type) {
        for (int year = 1991; year < 2020; year++) {
            for (int month = 1; month < 13; month++) {
                LOGGER.info(">>> Month: " + month + ", Year: " + year);
                RadiationCrawler radiationCrawler = new RadiationCrawler(month, year, type);
                radiationCrawler.download();
                radiationCrawler.unzip();
                radiationCrawler.insert(new RadiationRepository());
                radiationCrawler.delete();
            }
        }
    }
}
