package org.josmer.application.crawler;

import org.josmer.application.enums.RadiationTypes;
import org.josmer.application.repositories.RadiationRepository;

public class Insert {

    public static void main(String[] args) {
        if (!new RadiationRepository().isConnected()) {
            System.out.println("no db connection");
            return;
        }
        insertData(RadiationTypes.GLOBAL);
        insertData(RadiationTypes.DIRECT);
        insertData(RadiationTypes.DIFFUSE);
    }

    private static void insertData(RadiationTypes type) {
        for (int year = 1991; year < 2020; year++) {
            for (int month = 1; month < 13; month++) {
                System.out.println(">>> Month: " + month + ", Year: " + year);
                RadiationCrawler radiationCrawler = new RadiationCrawler(month, year, type);
                radiationCrawler.download();
                radiationCrawler.unzip();
                radiationCrawler.insert(new RadiationRepository());
                radiationCrawler.delete();
                System.out.println();
            }
        }
    }
}
