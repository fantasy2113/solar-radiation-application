package de.josmer.app.library.sun;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SplittableRandom;

class TagModel {
    private final SplittableRandom random;

    TagModel() {
        random = new SplittableRandom(LocalDateTime.now().getNano());
    }

    private void zeroGuard(double val) {
        if (val == 0) {
            throw new IllegalArgumentException("zero");
        }
    }

    double[] getDays(LocalDateTime month, double hGlob, double lat, double lon) {
        final int daysInMonth = CalcUtils.getDaysInMonth(month.getYear(), month.getMonthValue());
        double[] days = new double[daysInMonth];
        if (hGlob <= 0) {
            return days;
        }
        double[] dailyhe0Hor = new double[daysInMonth];
        double he0HorSum = 0.0;
        for (int d = 0; d < daysInMonth; d++) {
            SunPostion sunPos = new SunPostion();
            double sumSinGammaS = 0.0;
            for (int h = 0; h < 24; h++) {
                LocalDateTime dt = LocalDateTime.of(month.getYear(), month.getMonthValue(), d + 1, h, month.getMinute(), 0, 0);
                sunPos.calculate(dt, lat, lon);
                if (sunPos.getYsCorr() > 0) {
                    sumSinGammaS += CalcUtils.sin(sunPos.getYsCorr());
                }
            }
            final double he0Hor = e0OfDay(month) * sumSinGammaS;
            dailyhe0Hor[d] = he0Hor;
            he0HorSum += he0Hor;
        }
        zeroGuard(he0HorSum);
        for (int d = 0; d < daysInMonth; d++) {
            days[d] = hGlob * (dailyhe0Hor[d] / he0HorSum);
        }
        return days;
    }

    double[] getHours(LocalDateTime day, double hGlob, double lat, double lon) {
        if (hGlob <= 0) {
            return new double[24];
        }
        SunPostion sunPos = new SunPostion();
        double[] sunYOfh = new double[24];
        double sumSinGammaS = 0.0;
        for (int h = 0; h < 24; h++) {
            LocalDateTime dt = LocalDateTime.of(day.getYear(), day.getMonthValue(), day.getDayOfMonth(), h, day.getMinute(), 0, 0);
            sunPos.calculate(dt, lat, lon);
            sunYOfh[h] = sunPos.getYsCorr();
            if (sunPos.getYsCorr() > 0) {
                sumSinGammaS += CalcUtils.sin(sunPos.getYsCorr());
            }
        }
        double he0Hor = e0OfDay(day) * sumSinGammaS;
        zeroGuard(he0Hor);
        double kt = hGlob / he0Hor;
        double phi1 = 0.38 + 0.06 * Math.cos(7.4 * kt - 2.5);
        Map<Double, double[]> map = getMap(sunYOfh, he0Hor, kt, phi1);
        final Double minDiff = Collections.min(map.keySet());
        double[] retHours = map.get(minDiff);
        inrease(day, 1, 1.0991, retHours);
        inrease(day, 12, 1.2327, retHours);
        return retHours;
    }

    private void inrease(LocalDateTime day, int targetMonth, double val, double[] retHours) {
        if (day.getMonthValue() == targetMonth) {
            for (int h = 0; h < 24; h++) {
                retHours[h] = retHours[h] * val;
            }
        }
    }

    private Map<Double, double[]> getMap(double[] sunYOfh, double he0Hor, double kt, double phi1) {
        int cnt = 0;
        double[] ktOfh;
        double[] egHorOfh;
        double hSynHor;
        Map<Double, double[]> map = new HashMap<>();
        do {
            boolean isNoAdd = false;
            ++cnt;
            ktOfh = calcKtOfh(kt, phi1, sunYOfh);
            egHorOfh = new double[24];
            hSynHor = 0.0;
            for (int h = 0; h < ktOfh.length; h++) {
                double kth = ktOfh[h];
                if (kt == 0.0) {
                    continue; //NOSONAR
                }
                double ktMax = 0.88 * Math.cos((Math.PI * (h + 1.0 - 12.5)) / 30.0);
                if (kth < 0.0 || kth > ktMax) {
                    isNoAdd = true;
                    break; //NOSONAR
                } else {
                    egHorOfh[h] = ktOfh[h] * CalcUtils.EO_TAG * CalcUtils.sin(sunYOfh[h]);
                    hSynHor += egHorOfh[h];
                }
            }
            if (!isNoAdd) {
                double diff = Math.abs((hSynHor / he0Hor) - kt) / kt * 100.0;
                map.put(diff, egHorOfh);
            }
        } while (cnt < 10000);
        return map;
    }

    private double[] calcKtOfh(double kt, double phi1, double[] sunYOfh) {
        double[] yOfh = new double[24];
        double[] ktOfh = new double[24];
        for (int h = 0; h < 24; h++) {
            if (sunYOfh[h] > 0.0) {
                double ysDeg = CalcUtils.getRad(sunYOfh[h]);
                double ktm = -0.19 + 1.12 * kt + 0.24 * Math.exp(-8 * kt)
                        + (0.32 - 1.6 * Math.pow(kt - 0.5, 2))
                        * Math.exp((-0.19 - 2.27 * Math.pow(kt, 2) + 2.51 * Math.pow(kt, 3)) / Math.sin(ysDeg));
                double sigma = 0.14 * Math.exp(-20 * Math.pow(kt - 0.35, 2)) * Math.exp(3 * (Math.pow(kt - 0.45, 2) + 16 * Math.pow(kt, 5)) * (1 - Math.sin(ysDeg)));
                double z = random.nextDouble();
                double r = sigma * Math.sqrt(1 - Math.pow(phi1, 2)) * ((Math.pow(z, 0.135) - Math.pow(1 - z, 0.135)) / 0.1975);
                if (h > 0) {
                    yOfh[h] = phi1 * yOfh[h - 1] + r;
                } else {
                    yOfh[h] = r;
                }
                ktOfh[h] = ktm + sigma * yOfh[h];
            } else {
                ktOfh[h] = 0.0;
            }
        }
        return ktOfh;
    }

    private double e0OfDay(LocalDateTime day) {
        return CalcUtils.EO_TAG * (1.0 - 0.0334 * Math.cos(0.0172 * (double) day.getDayOfYear() - 0.04747));
    }
}
