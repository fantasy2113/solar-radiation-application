package org.josmer.utils;

public class GeoTrans {
    private double resultLp;
    private double resultBp;
    private double resultLw;
    private double resultBw;
    private double resultRw;
    private double resultHw;


    /**
     * Umrechnungen für das Bessel-Ellipsoid
     * <p>
     * Die beiden folgenden Funktionen rechnen für das Potsdam Kartendatum (Bessel Ellipsoid) geographische Koordinaten in Gauss-Krüger Koordinaten um und umgekehrt.
     * <p>
     * Mit der Funktion geoToGk.php lassen sich geographische Längen und Breiten für das Potsdam Kartendatum (Bessel-Ellipsoid) in deutsche Gauss-Krüger Koordinaten umrechnen:
     * <p>
     * <p>
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
     * um. Geographische Länge lp und Breite bp müssen im Potsdam Datum
     * gegeben sein. Berechnet werden Rechtswert resultRw und Hochwert resultHw.
     */
    public void geoToGk(final double lp, final double bp) {
        //Geographische Länge lp und Breite bp im Potsdam Datum
        // Grenzen des Gauss-Krüger-Systems für Deutschland 46° N < bp < 55° N, 5° E < lp < 16° E
        if (bp < 46 || bp > 56 || lp < 5 || lp > 16) {
            // Werte außerhalb des für Deutschland definierten Gauss-Krüger-Systems 5° E < LP < 16° E, 46° N < BP < 55° N
            this.resultRw = 0;
            this.resultHw = 0;
            return;
        }
        // Potsdam Datum
        // Große Halbachse a und Abplattung f
        double a = 6377397.155;
        double f = 3.34277321e-3;
        // Polkrümmungshalbmesser c
        double c = a / (1.0 - f);
        // Quadrat der zweiten numerischen Exzentrizität
        double ex2 = (2 * f - f * f) / ((1 - f) * (1 - f));
        double ex4 = ex2 * ex2;
        double ex6 = ex4 * ex2;
        double ex8 = ex4 * ex4;
        // Koeffizienten zur Berechnung der Meridianbogenlänge
        double e0 = c * (Math.PI / 180) * (1 - 3 * ex2 / 4 + 45 * ex4 / 64 - 175 * ex6 / 256 + 11025 * ex8 / 16384);
        double e2 = c * (-3 * ex2 / 8 + 15 * ex4 / 32 - 525 * ex6 / 1024 + 2205 * ex8 / 4096);
        double e4 = c * (15 * ex4 / 256 - 105 * ex6 / 1024 + 2205 * ex8 / 16384);
        double e6 = c * (-35 * ex6 / 3072 + 315 * ex8 / 12288);
        // Breite in Radianten
        double br = bp * Math.PI / 180;
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
        double nd = c / Math.sqrt(1 + etasq);
        // Meridianbogenlänge g aus gegebener geographischer Breite bp
        double g = e0 * bp + e2 * Math.sin(2 * br) + e4 * Math.sin(4 * br) + e6 * Math.sin(6 * br);
        // Längendifferenz dl zum Bezugsmeridian lh
        double kz = (int) ((lp + 1.5) / 3.0);
        double lh = kz * 3;
        double dl = (lp - lh) * Math.PI / 180;
        double dl2 = dl * dl;
        double dl4 = dl2 * dl2;
        double dl3 = dl2 * dl;
        double dl5 = dl4 * dl;
        // Hochwert resultHw und Rechtswert resultRw als Funktion von geographischer Breite und Länge
        this.resultHw = (g + nd * cos2 * tan1 * dl2 / 2 + nd * cos4 * tan1 * (5 - tan2 + 9 * etasq) * dl4 / 24);
        this.resultRw = (nd * cos1 * dl + nd * cos3 * (1 - tan2 + etasq) * dl3 / 6 + nd * cos5 * (5 - 18 * tan2 + tan4) * dl5 / 120 + kz * 1e6 + 500000);
        double nk = this.resultHw - (int) this.resultHw;
        if (nk < 0.5) {
            this.resultHw = (int) this.resultHw;
        } else {
            this.resultHw = (int) (this.resultHw) + 1;
        }
        nk = this.resultRw - (int) this.resultRw;
        if (nk < 0.5) {
            this.resultRw = (int) this.resultRw;
        } else {
            this.resultRw = (int) this.resultRw + 1;
        }
    }

    /*Mit der
    Funktion gk2geo.
    php lassen
    sich Gauss-
    Krüger Koordinaten
    in geographische
    Längen und
    Breiten des
    Potsdam Datums
    umrechnen:

     Copyright (c) 2007, HELMUT H. HEIMEIER
   Permission is hereby granted, free of charge, to any person obtaining a
   copy of this software and associated documentation files (the "Software"),
   to deal in the Software without restriction, including without limitation
   the rights to use, copy, modify, merge, publish, distribute, sublicense,
   and/or sell copies of the Software, and to permit persons to whom the
   Software is furnished to do so, subject to the following conditions:
   The above copyright notice and this permission notice shall be included
   in all copies or substantial portions of the Software.

Die Funktion wandelt GK Koordinaten in geographische Koordinaten
   um. Rechtswert resultRw und Hochwert resultHw müssen gegeben sein.
   Berechnet werden geographische Länge lp und Breite bp
   im Potsdam Datum.


    function gk2geo() {


   global $resultRw, $resultHw, $lp, $bp, $lw, $bw, $zone, $ew, $nw, $raster, $ew2, $nw2;

// Rechtswert resultRw und Hochwert resultHw im Potsdam Datum
   if ($resultRw == "" || $resultHw == "")
   {
   $lp = "";
   $bp = "";
   return;
   }

   $resultRw = intval($resultRw);
   $resultHw = intval($resultHw);

// Potsdam Datum
// Große Halbachse a und Abplattung f
   $a = 6377397.155;
   $f = 3.34277321e-3;
// Polkrümmungshalbmesser c
   $c = $a/(1-$f);

// Quadrat der zweiten numerischen Exzentrizität
   $ex2 = (2*$f-$f*$f)/((1-$f)*(1-$f));
   $ex4 = $ex2*$ex2;
   $ex6 = $ex4*$ex2;
   $ex8 = $ex4*$ex4;

// Koeffizienten zur Berechnung der geographischen Breite aus gegebener
// Meridianbogenlänge
   $e0 = $c*(pi()/180)*(1 - 3*$ex2/4 + 45*$ex4/64 - 175*$ex6/256 + 11025*$ex8/16384);
   $f2 =    (180/pi())*(    3*$ex2/8 - 3*$ex4/16  + 213*$ex6/2048 -  255*$ex8/4096);
   $f4 =               (180/pi())*(   21*$ex4/256 -  21*$ex6/256  +  533*$ex8/8192);
   $f6 =                             (180/pi())*(   151*$ex6/6144 -  453*$ex8/12288);

// Geographische Breite bf zur Meridianbogenlänge gf = resultHw
   $sigma = $resultHw/$e0;
   $sigmr = $sigma*pi()/180;
   $bf = $sigma + $f2*sin(2*$sigmr) + $f4*sin(4*$sigmr) + $f6*sin(6*$sigmr);

// Breite bf in Radianten
   $br = $bf * pi()/180;
   $tan1 = tan($br);
   $tan2 = $tan1*$tan1;
   $tan4 = $tan2*$tan2;

   $cos1 = cos($br);
   $cos2 = $cos1*$cos1;

   $etasq = $ex2*$cos2;

// Querkrümmungshalbmesser nd
   $nd = $c/sqrt(1 + $etasq);
   $nd2 = $nd*$nd;
   $nd4 = $nd2*$nd2;
   $nd6 = $nd4*$nd2;
   $nd3 = $nd2*$nd;
   $nd5 = $nd4*$nd;

//  Längendifferenz dl zum Bezugsmeridian lh
   $kz = intval($resultRw/1e6);
   $lh = $kz*3;
   $dy = $resultRw-($kz*1e6+500000);
   $dy2 = $dy*$dy;
   $dy4 = $dy2*$dy2;
   $dy3 = $dy2*$dy;
   $dy5 = $dy4*$dy;
   $dy6 = $dy3*$dy3;

   $b2 = - $tan1*(1+$etasq)/(2*$nd2);
   $b4 =   $tan1*(5+3*$tan2+6*$etasq*(1-$tan2))/(24*$nd4);
   $b6 = - $tan1*(61+90*$tan2+45*$tan4)/(720*$nd6);

   $l1 =   1/($nd*$cos1);
   $l3 = - (1+2*$tan2+$etasq)/(6*$nd3*$cos1);
   $l5 =   (5+28*$tan2+24*$tan4)/(120*$nd5*$cos1);

// Geographischer Breite bp und Länge lp als Funktion von Rechts- und Hochwert
   $bp = $bf + (180/pi()) * ($b2*$dy2 + $b4*$dy4 + $b6*$dy6);
   $lp = $lh + (180/pi()) * ($l1*$dy  + $l3*$dy3 + $l5*$dy5);

   if ($lp < 5 || $lp > 16 || $bp < 46 || $bp > 56)
   {
   echo("RW und/oder HW ungültig für das deutsche Gauss-Krüger-System
\n");
   $lp = "";
   $bp = "";
   }
   return;
}*/


    /**
     * Funktionen zur Änderung des Kartenbezugssystems WGS84 - Potsdam
     * <p>
     * Mit den beiden folgenden Funktionen lässt sich das Kartenbezugsdatum vom WGS84 System auf das Potsdam Datum (Zentralpunkt Rauenberg) und umgekehrt verschieben. Bei der Transformation werden die Ellipsoidachsen parallel verschoben um dx = 587 m, dy = 16 m und dz = 393 m. Hierbei liegt der Koordinatenursprung im Erdmittelpunkt, die positive x-Achse schneidet Äquator und Nullmeridian, die positive y-Achse schneidet Äquator und 90 grad E Meridian und die positive z-Achse schneidet den Nordpol. Die shift-Parameter dx, dy und dz sind so gewählt, dass sich bei der Umformung übereinstimmung mit den von Garmin GPS-Geräten ermittelten Werten ergibt.
     * <p>
     * Man beachte, dass in der deutschen Landesvermessung im allgemeinen mit anderen Parametern gerechnet wird, nämlich mit dx = 631 m, dy = 23 m und dz = 451 m.
     * <p>
     * Die Funktion wgs2pot.php formt geographischen Längen und Breiten des WGS84 Datums in solche des Potsdam Datums um:
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
     * Die Funktion verschiebt das Kartenbezugssystem (map datum) vom
     * WGS84 Datum (World Geodetic System 84) zum in Deutschland
     * gebräuchlichen Potsdam-Datum. Geographische Länge resultLw und Breite
     * resultBw gemessen in grad auf dem WGS84 Ellipsoid müssen
     * gegeben sein. Ausgegeben werden geographische Länge resultLp
     * und Breite resultBp (in grad) auf dem Bessel-Ellipsoid.
     * Bei der Transformation werden die Ellipsoidachsen parallel
     * verschoben um dx = -587 m, dy = -16 m und dz = -393 m.
     * <p>
     *
     * @param lw Geographische Länge
     * @param bw Geographische Breite
     */
    public void wgs84ToPotsdamDatum(final double lw, final double bw) {
        // Große Halbachse a und Abplattung fq
        double a = 6378137.000;
        double fq = 3.35281066e-3;
        // Zielsystem Potsdam Datum
        // Abplattung f
        double f = fq - 1.003748e-5;
        // Parameter für datum shift
        double dx = -587.0;
        double dy = -16.0;
        double dz = -393.0;
        // Quadrat der ersten numerischen Exzentrizität in Quell- und Zielsystem
        double e2q = (2.0 * fq - fq * fq);
        double e2 = (2.0 * f - f * f);
        // Breite und Länge in Radianten
        double b1 = bw * (Math.PI / 180.0);
        double l1 = lw * (Math.PI / 180.0);
        // Querkrümmungshalbmesser nd
        double nd = a / Math.sqrt(1 - e2q * Math.sin(b1) * Math.sin(b1));
        // Kartesische Koordinaten des Quellsystems WGS84
        double xw = nd * Math.cos(b1) * Math.cos(l1);
        double yw = nd * Math.cos(b1) * Math.sin(l1);
        double zw = (1.0 - e2q) * nd * Math.sin(b1);
        // Kartesische Koordinaten des Zielsystems (datum shift) Potsdam
        double x = xw + dx;
        double y = yw + dy;
        double z = zw + dz;
        // Berechnung von Breite und Länge im Zielsystem
        double rb = Math.sqrt(x * x + y * y);
        double b2 = (180.0 / Math.PI) * Math.atan((z / rb) / (1.0 - e2));
        double l2 = 0.0;
        if (x > 0) {
            l2 = (180 / Math.PI) * Math.atan(y / x);
        } else if (x < 0 && y > 0) {
            l2 = (180 / Math.PI) * Math.atan(y / x) + 180;
        } else if (x < 0 && y < 0) {
            l2 = (180 / Math.PI) * Math.atan(y / x) - 180;
        }
        this.resultLp = l2;
        this.resultBp = b2;
        if (resultLp < 5 || resultLp > 16 || resultBp < 46 || resultBp > 56) {
            resultLp = Double.NaN;
            resultBp = Double.NaN;
        }
    }

    /**
     * Die Funktion formt geographische Längen und Breiten des Potsdam Datums in solche des WGS84 Datums um:
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
     * Die Funktion verschiebt das Kartenbezugssystem (map datum) vom in
     * Deutschland gebräuchlichen Potsdam-Datum zum WGS84 (World Geodetic
     * System 84) Datum. Geographische Länge resultLp und Breite resultBp gemessen in
     * grad auf dem Bessel-Ellipsoid müssen gegeben sein.
     * Ausgegeben werden geographische Länge resultLw und
     * Breite resultBw (in grad) auf dem WGS84-Ellipsoid.
     * Bei der Transformation werden die Ellipsoidachsen parallel
     * verschoben um dx = 587 m, dy = 16 m und dz = 393 m.
     * <p>
     *
     * @param lp
     * @param bp
     */
    public void potsdamDatumToWgs84(final double lp, final double bp) {
        // Quellsystem Potsdam Datum
        //  Große Halbachse a und Abplattung fq
        double a = 6378137.000 - 739.845;
        double fq = 3.35281066e-3 - 1.003748e-05;
        // Zielsystem WGS84 Datum
        //  Abplattung f
        double f = 3.35281066e-3;
        // Parameter für datum shift
        double dx = 587.0;
        double dy = 16.0;
        double dz = 393.0;
        // Quadrat der ersten numerischen Exzentrizität in Quell- und Zielsystem
        double e2q = (2.0 * fq - fq * fq);
        double e2 = (2.0 * f - f * f);
        // Breite und Länge in Radianten
        double b1 = bp * (Math.PI / 180.0);
        double l1 = lp * (Math.PI / 180.0);
        // Querkrümmungshalbmesser nd
        double nd = a / Math.sqrt(1.0 - e2q * Math.sin(b1) * Math.sin(b1));
        // Kartesische Koordinaten des Quellsystems Potsdam
        double xp = nd * Math.cos(b1) * Math.cos(l1);
        double yp = nd * Math.cos(b1) * Math.sin(l1);
        double zp = (1.0 - e2q) * nd * Math.sin(b1);
        // Kartesische Koordinaten des Zielsystems (datum shift) WGS84
        double x = xp + dx;
        double y = yp + dy;
        double z = zp + dz;
        // Berechnung von Breite und Länge im Zielsystem
        double rb = Math.sqrt(x * x + y * y);
        double b2 = (180.0 / Math.PI) * Math.atan((z / rb) / (1 - e2));
        double l2 = 0.0;
        if (x > 0) {
            l2 = (180 / Math.PI) * Math.atan(y / x);
        } else if (x < 0 && y > 0) {
            l2 = (180 / Math.PI) * Math.atan(y / x) + 180.0;
        } else if (x < 0 && y < 0) {
            l2 = (180 / Math.PI) * Math.atan(y / x) - 180.0;
        }
        this.resultLw = l2;
        this.resultBw = b2;
    }

    /**
     * @return result of wgs84ToPotsdamDatum(...)
     */
    public double getResultLp() {
        return resultLp;
    }

    /**
     * @return result of wgs84ToPotsdamDatum(...)
     */
    public double getResultBp() {
        return resultBp;
    }

    /**
     * @return result of potsdamDatumToWgs84(...)
     */
    public double getResultLw() {
        return resultLw;
    }

    /**
     * @return result of potsdamDatumToWgs84(...)
     */
    public double getResultBw() {
        return resultBw;
    }

    public double getResultRw() {
        return resultRw;
    }

    public double getResultHw() {
        return resultHw;
    }
}
