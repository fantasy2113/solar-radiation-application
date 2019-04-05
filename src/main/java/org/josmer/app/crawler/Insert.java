package org.josmer.app.crawler;

import org.josmer.app.core.RadiationTypes;
import org.josmer.app.repository.RadiationRepository;

public class Insert {

    private static String dbUrl = "postgres://ntdzcywnkjukss:cd01495e80208c5d6961118ef26bd5e0edf15e3cfa906f33ba018a85940447cf@ec2-54-246-92-116.eu-west-1.compute.amazonaws.com:5432/d81ptonl3ncqr9";

    public static void main(String[] args) {
        if (!new RadiationRepository(dbUrl).isConnected()) {
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
                radiationCrawler.insert(dbUrl);
                radiationCrawler.delete();
                System.out.println();
            }
        }
    }
}
