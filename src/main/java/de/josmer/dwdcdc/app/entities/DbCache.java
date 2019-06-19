package de.josmer.dwdcdc.app.entities;

import de.josmer.dwdcdc.app.interfaces.IJsonb;

import java.util.LinkedList;

public class DbCache implements IJsonb {
    private int id;
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

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public int getId() {
        return getKey().hashCode();
    }

    public void setKey(String key) {
        this.key = key;
    }

    public LinkedList<SolIrrExp> getYear() {
        return year;
    }

    public void setYear(LinkedList<SolIrrExp> year) {
        this.year = year;
    }

    public void setId(int id) {
        this.id = id;
    }
}
