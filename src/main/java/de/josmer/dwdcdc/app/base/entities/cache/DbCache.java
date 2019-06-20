package de.josmer.dwdcdc.app.base.entities.cache;

import de.josmer.dwdcdc.app.base.entities.SolIrrExp;

import java.time.LocalDateTime;
import java.util.LinkedList;

public class DbCache {
    private int id;
    private String key;
    private LocalDateTime created;
    private LinkedList<SolIrrExp> months;

    public DbCache(String key, LinkedList<SolIrrExp> months) {
        this.key = key;
        this.months = months;
        this.id = this.key.hashCode();
        this.created = LocalDateTime.now();
    }

    public DbCache() {
        this.key = "";
        this.months = new LinkedList<>();
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
