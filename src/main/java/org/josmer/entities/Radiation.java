package org.josmer.entities;

import org.springframework.data.annotation.Id;

public class Radiation {
    @Id
    public String id;
    public int month;
    public int year;
    public int gkNorth;
    public int gkSouth;
    public double radiation;

}
