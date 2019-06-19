package de.josmer.dwdcdc.app.entities;

import java.util.LinkedList;

public class DbCache {
    private String key;
    private LinkedList<SolIrrExp> year;

    public DbCache(String key, LinkedList<SolIrrExp> year) {
        this.key = key;
        this.year = year;
    }

    public DbCache() {
        this.key = "";
        this.year = new LinkedList<>();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public LinkedList<Double> getYear() {
        return year;
    }

    public void setYear(LinkedList<Double> year) {
        this.year = year;
    }
}
