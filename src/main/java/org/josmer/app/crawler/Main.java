package org.josmer.app.crawler;

import org.josmer.app.core.RadiationTypes;

public class Main {

    public static void main(String[] args) {
        for (int year = 2019; year < 2020; year++) {
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
