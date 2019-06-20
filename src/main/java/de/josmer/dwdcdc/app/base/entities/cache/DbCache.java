package de.josmer.dwdcdc.app.base.entities.cache;

import de.josmer.dwdcdc.app.base.entities.SolIrrExp;
import de.josmer.dwdcdc.app.base.interfaces.IKey;

import java.util.LinkedList;

public class DbCache implements IKey {
    private String key;
    private LinkedList<SolIrrExp> months;

    public DbCache(String key, LinkedList<SolIrrExp> months) {
        this.key = key;
        this.months = months;
    }

    public DbCache() {
        this.key = "";
        this.months = new LinkedList<>();
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public LinkedList<SolIrrExp> getMonths() {
        return months;
    }

    public void setMonths(LinkedList<SolIrrExp> months) {
        this.months = months;
    }
}
