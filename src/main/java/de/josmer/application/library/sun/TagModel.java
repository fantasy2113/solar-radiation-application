package de.josmer.application.library.sun;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

class TagModel {
    private static final double E0 = 1367.0;
    private Random random;

    TagModel() {
        random = new Random(LocalDateTime.now().getNano());
    }

    private static double degreeToRad(double deg) {
        return deg * Math.PI / 180;
    }

    double[] calculateDay(LocalDateTime day, double hGlob, double lat, double lon) {
        SubPostion sunPos = new SubPostion();
        double[] sunYOfh = new double[24];
        double sumSinGammaS = 0.0;
        for (int h = 0; h < 24; h++) {
            LocalDateTime dt = LocalDateTime.of(day.getYear(), day.getMonthValue(), day.getDayOfMonth(), h, day.getMinute(), 0, 0);
            sunPos.calculate(dt, lat, lon, 1);
            sunYOfh[h] = sunPos.getYsAtmosphericRefractionCorrection();
            if (sunPos.getYsAtmosphericRefractionCorrection() > 0) {
                sumSinGammaS += Math.sin(degreeToRad(sunPos.getYsAtmosphericRefractionCorrection()));
            }
        }
        double he0Hor = e0OfDay(day) * sumSinGammaS;
        double kt = hGlob / he0Hor;
        double phi1 = 0.38 + 0.06 * Math.cos(7.4 * kt - 2.5);
        int cnt = 0;
        double[] KtOfh;
        double[] EgHorOfh;
        double HSynHor;
        Map<Double, double[]> map = new HashMap<>();
        do {
            boolean isAdd = true;
            ++cnt;
            KtOfh = calcKtOfh(lat, lon, kt, phi1, sunYOfh);
            EgHorOfh = new double[24];
            HSynHor = 0.0;
            for (int h = 0; h < KtOfh.length; h++) {
                double kth = KtOfh[h];
                if (kth == 0.0) {
                    continue;
                }
                double ktMax = 0.88 * Math.cos((Math.PI * (h + 1 - 12.5)) / 30.0);
                if (kth < 0.0 || kth > ktMax) {
                    isAdd = false;
                    break;
                }
                EgHorOfh[h] = KtOfh[h] * E0 * Math.sin(degreeToRad(sunYOfh[h]));
                HSynHor += EgHorOfh[h];
            }
            if (isAdd) {
                double diff = Math.abs((HSynHor / he0Hor) - kt) / kt * 100.0;
                map.put(diff, EgHorOfh);
            }
        } while (cnt < 10000);
        final Double minDiff = Collections.min(map.keySet());
        return map.get(minDiff);
    }

    private double[] calcKtOfh(double lat, double lon, double Kt, double phi1, double[] sunYOfh) {
        double[] yOfh = new double[24];
        double[] KtOfh = new double[24];
        for (int h = 0; h < 24; h++) {
            if (sunYOfh[h] > 0.0) {
                double YsDeg = degreeToRad(sunYOfh[h]);
                double Ktm = -0.19 + 1.12 * Kt + 0.24 * Math.exp(-8 * Kt)
                        + (0.32 - 1.6 * Math.pow(Kt - 0.5, 2))
                        * Math.exp((-0.19 - 2.27 * Math.pow(Kt, 2) + 2.51 * Math.pow(Kt, 3)) / Math.sin(YsDeg));
                double sigma = 0.14 * Math.exp(-20 * Math.pow(Kt - 0.35, 2)) * Math.exp(3 * (Math.pow(Kt - 0.45, 2) + 16 * Math.pow(Kt, 5)) * (1 - Math.sin(YsDeg)));
                double z = random.nextDouble();
                double r = sigma * Math.sqrt(1 - Math.pow(phi1, 2)) * ((Math.pow(z, 0.135) - Math.pow(1 - z, 0.135)) / 0.1975);
                if (h > 0) {
                    yOfh[h] = phi1 * yOfh[h - 1] + r;
                } else {
                    yOfh[h] = r;
                }
                KtOfh[h] = Ktm + sigma * yOfh[h];
            } else {
                KtOfh[h] = 0.0;
            }
        }
        return KtOfh;
    }

    private double e0OfDay(LocalDateTime day) {
        return E0 * (1 - 0.0334 * Math.cos(0.0172 * (double) day.getDayOfYear() - 0.04747));
    }
}
