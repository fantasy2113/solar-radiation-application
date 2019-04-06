package org.josmer.app.crawler;

import org.josmer.app.core.RadiationTypes;
import org.josmer.app.repository.MonthlyRadiationRepository;

public class Insert {

    public static void main(String[] args) {
        if (!new MonthlyRadiationRepository().isConnected()) {
            System.err.println("no db connection");
            return;
        }
        insertData();
    }

    private static void insertData() {
        for (int year = 1991; year < 2019; year++) {
            for (int month = 1; month < 13; month++) {
                System.out.println(">>> Month: " + month + ", Year: " + year);
                MonthlyRadiationCrawler monthlyRadiationCrawler = new MonthlyRadiationCrawler(month, year, RadiationTypes.DIRECT);
                monthlyRadiationCrawler.download();
                monthlyRadiationCrawler.unzip();
                monthlyRadiationCrawler.insert();
                monthlyRadiationCrawler.delete();
                System.out.println();
            }
        }
    }
}
