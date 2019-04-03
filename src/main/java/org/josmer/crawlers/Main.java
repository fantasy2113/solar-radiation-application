package org.josmer.crawlers;

public class Main {

    public static void main(String[] args) {
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
