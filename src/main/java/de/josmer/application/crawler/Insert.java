package de.josmer.application.crawler;

import de.josmer.application.enums.RadiationTypes;
import de.josmer.application.repositories.RadiationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Insert {
    private static final Logger LOGGER = LogManager.getLogger(Insert.class.getName());

    public static void main(String[] args) {
        if (!new RadiationRepository().isConnected()) {
            LOGGER.info("no db connection");
            return;
        }
        insertData(RadiationTypes.GLOBAL);
        insertData(RadiationTypes.DIRECT);
        insertData(RadiationTypes.DIFFUSE);
    }

    public static void insertData(RadiationTypes type) {
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
