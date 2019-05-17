package de.josmer.application.libraries.utils;

import de.josmer.application.libraries.crawler.RadiationCrawler;
import de.josmer.application.libraries.enums.RadTypes;
import de.josmer.application.repositories.SolRadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsertMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(InsertMain.class.getName());

    public static void main(String[] args) {
        insertData(RadTypes.GLOBAL);
        insertData(RadTypes.DIRECT);
        insertData(RadTypes.DIFFUSE);
    }

    private static void insertData(RadTypes type) {
        for (int year = 1991; year < 2020; year++) {
            for (int month = 1; month < 13; month++) {
                LOGGER.info(">>> Month: " + month + ", Year: " + year);
                RadiationCrawler radiationCrawler = new RadiationCrawler(month, year, type);
                radiationCrawler.insert(new SolRadRepository(), new FileReader());
            }
        }
    }
}
