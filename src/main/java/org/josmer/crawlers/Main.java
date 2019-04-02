package org.josmer.crawlers;

import org.josmer.security.Connector;

public class Main {

    public static void main(String[] args) throws Exception {
        Connector.setPassword("qw999");
        Connector.setUser("julian");
        Connector.setUrl("jdbc:mysql://localhost:3306/solar_test");

        for (int year = 1991; year < 2020; year++) {
            for (int month = 1; month < 13; month++) {
                System.out.println(">>> Month: " + month + ", Year: " + year);
                RadiationCrawler radiationCrawler = new RadiationCrawler(month, year, "GLOBAL");
                radiationCrawler.download();
                radiationCrawler.unzip();
                radiationCrawler.insert();
                radiationCrawler.delete();
                System.out.println();
            }
        }

    }
}
