package org.josmer.app.crawler;

import org.josmer.app.core.RadiationTypes;
import org.josmer.app.repository.RadiationRepository;

public class Insert {

    public static void main(String[] args) {
        if (!new RadiationRepository().isConnected()) {
            System.out.println("no db connection");
            return;
        }
        insertData();
    }

    private static void insertData() {
        for (int year = 1991; year < 2020; year++) {
            for (int month = 1; month < 13; month++) {
                System.out.println(">>> Month: " + month + ", Year: " + year);
                RadiationCrawler radiationCrawler = new RadiationCrawler(month, year, RadiationTypes.GLOBAL);
                radiationCrawler.download();
                radiationCrawler.unzip();
                radiationCrawler.insert(new RadiationRepository());
                radiationCrawler.delete();
                System.out.println();
            }
        }
    }
}
