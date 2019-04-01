package org.josmer.crawlers;

import org.josmer.interfaces.ICrawler;

public final class DwdCrawler {

    private final ICrawler crawler;

    public DwdCrawler(final ICrawler crawler) {
        this.crawler = crawler;
    }

    public void insertFromYears(final int startYear, final int endYear) {
        try {
            for (int year = startYear; year <= endYear; year++) {
                for (int month = 1; month <= 12; month++) {
                    insert(getDate(year, month));
                }
            }
        } catch (Exception e) {

        }
    }


    public void insertFromDate(final String date) {
        try {
            insert(date);
        } catch (Exception e) {

        }
    }

    private void insert(final String date) throws Exception {
        crawler.download(date);
        crawler.unzip();
        crawler.insert();
        crawler.delete();
    }

    private String getDate(final Integer year, final Integer month) {
        StringBuilder date = new StringBuilder();
        date.append(year);
        if (month.toString().length() == 2) {
            date.append(month);
        } else {
            date.append(0);
            date.append(month);
        }
        return date.toString();
    }
}
