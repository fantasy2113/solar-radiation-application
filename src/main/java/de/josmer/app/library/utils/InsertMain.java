package de.josmer.app.library.utils;

import de.josmer.app.library.crawler.RadiationCrawler;
import de.josmer.app.library.enums.RadiationTypes;
import de.josmer.app.model.repositories.SolRadiRepository;
import de.josmer.app.model.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsertMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(InsertMain.class.getName());

    public static void main(String[] args) {
        insertData(RadiationTypes.GLOBAL);
        insertData(RadiationTypes.DIRECT);
        insertData(RadiationTypes.DIFFUSE);
        new UserRepository().saveUser("user", "abc123");
    }

    private static void insertData(RadiationTypes type) {
        for (int year = 2017; year < 2020; year++) {
            for (int month = 1; month < 13; month++) {
                LOGGER.info(">>> Month: " + month + ", Year: " + year);
                RadiationCrawler radiationCrawler = new RadiationCrawler(month, year, type);
                radiationCrawler.download();
                radiationCrawler.unzip();
                radiationCrawler.insert(new SolRadiRepository());
                radiationCrawler.delete();
            }
        }
    }
}
