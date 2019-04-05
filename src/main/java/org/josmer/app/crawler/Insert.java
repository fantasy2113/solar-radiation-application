package org.josmer.app.crawler;

import org.josmer.app.core.RadiationTypes;
import org.josmer.app.repository.RadiationRepository;

public class Insert {

    public static void main(String[] args) {
        if (!new RadiationRepository().isConnected()) {
            System.err.println("no db connection");
            return;
        }
        insertData();
    }

    private static void insertData() {
        for (int year = 2018; year < 2019; year++) {
            for (int month = 1; month < 13; month++) {
                System.out.println(">>> Month: " + month + ", Year: " + year);
                RadiationCrawler radiationCrawler = new RadiationCrawler(month, year, RadiationTypes.GLOBAL);
                radiationCrawler.download();
                radiationCrawler.unzip();
                radiationCrawler.insert();
                radiationCrawler.delete();
                System.out.println();
            }
        }
    }
}
