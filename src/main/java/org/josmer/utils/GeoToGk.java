package org.josmer.utils;

public class GeoToGk {
    private final double lon;
    private final double lat;
    private double rechtswert;
    private double hochwert;

    public GeoToGk(final double lon, final double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    /**
     * Umrechnungen für das Bessel-Ellipsoid
     * <p>
     * Die beiden folgenden Funktionen rechnen für das Potsdam Kartendatum (Bessel Ellipsoid) geographische Koordinaten in Gauss-Krüger Koordinaten um und umgekehrt.
     * <p>
     * Mit der Funktion calculate.php lassen sich geographische Längen und Breiten für das Potsdam Kartendatum (Bessel-Ellipsoid) in deutsche Gauss-Krüger Koordinaten umrechnen:
     * <p>
     * Copyright (c) 2006, HELMUT H. HEIMEIER
     * Permission is hereby granted, free of charge, to any person obtaining a
     * copy of this software and associated documentation files (the "Software"),
     * to deal in the Software without restriction, including without limitation
     * the rights to use, copy, modify, merge, publish, distribute, sublicense,
     * and/or sell copies of the Software, and to permit persons to whom the
     * Software is furnished to do so, subject to the following conditions:
     * The above copyright notice and this permission notice shall be included
     * in all copies or substantial portions of the Software.
     * <p>
     * Die Funktion wandelt geographische Koordinaten in GK Koordinaten
     * um. Geographische Länge lon und Breite lat müssen im Potsdam Datum
     * gegeben sein. Berechnet werden Rechtswert rechtswert und Hochwert hochwert.
     */
    public void calculate() {
        //Geographische Länge lon und Breite lat im Potsdam Datum
        // Grenzen des Gauss-Krüger-Systems für Deutschland 46° N < lat < 55° N, 5° E < lon < 16° E
        if (this.lat < 46 || this.lat > 56 || this.lon < 5 || this.lon > 16) {
            // Werte außerhalb des für Deutschland definierten Gauss-Krüger-Systems 5° E < LP < 16° E, 46° N < BP < 55° N
            this.rechtswert = 0.0;
            this.hochwert = 0.0;
            return;
        }
        // Potsdam Datum
        // Große Halbachse a und Abplattung f
        double a = 6377397.155;
        double f = 3.34277321e-3;
        // Polkrümmungshalbmesser c
        double c = a / (1.0 - f);
        // Quadrat der zweiten numerischen Exzentrizität
        double ex2 = (2.0 * f - f * f) / ((1.0 - f) * (1.0 - f));
        double ex4 = ex2 * ex2;
        double ex6 = ex4 * ex2;
        double ex8 = ex4 * ex4;
        // Koeffizienten zur Berechnung der Meridianbogenlänge
        double e0 = c * (Math.PI / 180) * (1.0 - 3.0 * ex2 / 4.0 + 45.0 * ex4 / 64.0 - 175.0 * ex6 / 256.0 + 11025.0 * ex8 / 16384.0);
        double e2 = c * (-3.0 * ex2 / 8.0 + 15.0 * ex4 / 32 - 525 * ex6 / 1024.0 + 2205.0 * ex8 / 4096.0);
        double e4 = c * (15.0 * ex4 / 256.0 - 105.0 * ex6 / 1024.0 + 2205.0 * ex8 / 16384.0);
        double e6 = c * (-35.0 * ex6 / 3072.0 + 315.0 * ex8 / 12288.0);
        // Breite in Radianten
        double br = this.lat * Math.PI / 180.0;
        double tan1 = Math.tan(br);
        double tan2 = tan1 * tan1;
        double tan4 = tan2 * tan2;
        double cos1 = Math.cos(br);
        double cos2 = cos1 * cos1;
        double cos4 = cos2 * cos2;
        double cos3 = cos2 * cos1;
        double cos5 = cos4 * cos1;
        double etasq = ex2 * cos2;
        // Querkrümmungshalbmesser nd
        double nd = c / Math.sqrt(1.0 + etasq);
        // Meridianbogenlänge g aus gegebener geographischer Breite lat
        double g = e0 * this.lat + e2 * Math.sin(2 * br) + e4 * Math.sin(4.0 * br) + e6 * Math.sin(6.0 * br);
        // Längendifferenz dl zum Bezugsmeridian lh
        double kz = (int) ((this.lon + 1.5) / 3.0);
        double lh = kz * 3.0;
        double dl = (this.lon - lh) * Math.PI / 180.0;
        double dl2 = dl * dl;
        double dl4 = dl2 * dl2;
        double dl3 = dl2 * dl;
        double dl5 = dl4 * dl;
        // Hochwert hochwert und Rechtswert rechtswert als Funktion von geographischer Breite und Länge
        this.hochwert = (g + nd * cos2 * tan1 * dl2 / 2.0 + nd * cos4 * tan1 * (5.0 - tan2 + 9.0 * etasq) * dl4 / 24.0);
        this.rechtswert = (nd * cos1 * dl + nd * cos3 * (1.0 - tan2 + etasq) * dl3 / 6.0 + nd * cos5 * (5.0 - 18.0 * tan2 + tan4) * dl5 / 120.0 + kz * 1e6 + 500000.0);
        double nk = this.hochwert - (int) this.hochwert;
        if (nk < 0.5) {
            this.hochwert = (int) this.hochwert;
        } else {
            this.hochwert = (int) (this.hochwert) + 1;
        }
        nk = this.rechtswert - (int) this.rechtswert;
        if (nk < 0.5) {
            this.rechtswert = (int) this.rechtswert;
        } else {
            this.rechtswert = (int) this.rechtswert + 1;
        }
    }

    public double getRechtswert() {
        return rechtswert;
    }

    public double getHochwert() {
        return hochwert;
    }
}
