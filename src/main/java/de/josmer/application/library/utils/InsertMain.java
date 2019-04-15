package de.josmer.application.library.utils;

import de.josmer.application.library.crawler.RadiationCrawler;
import de.josmer.application.library.enums.RadiationTypes;
import de.josmer.application.repositories.RadiationRepository;
import de.josmer.application.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class InsertMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsertMain.class.getName());

    public static void main(String[] args) {
        //insertData(RadiationTypes.GLOBAL);
        //insertData(RadiationTypes.DIRECT);
        //insertData(RadiationTypes.DIFFUSE);
        new UserRepository().saveUser("user", "abc123");
    }

    public static void insertData(RadiationTypes type) {
        for (int year = 1991; year < 2020; year++) {
            for (int month = 1; month < 13; month++) {
                LOGGER.info(">>> Month: " + month + ", Year: " + year);
                RadiationCrawler radiationCrawler = new RadiationCrawler(month, year, type);
                radiationCrawler.download();
                radiationCrawler.unzip();
                radiationCrawler.insert(new RadiationRepository("postgres://u9q7kd79d0eodj:pcea69f8d94f3a7af2e3fb3e415a6b4bba5e972ba89efa5db298adf6854d778f3@ec2-52-208-123-217.eu-west-1.compute.amazonaws.com:5432/d2jiiehmaovnt3"));
                radiationCrawler.delete();
            }
        }
    }
}
