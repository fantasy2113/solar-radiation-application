package de.jos.dwdcdc.library.irradiation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SplittableRandom;

/**
 * Beware: Not Thread-Safety because of SplittableRandom
 */
final class SolarSynthesiser {
  private static final Logger LOGGER = LoggerFactory.getLogger(SolarSynthesiser.class.getName());

  private final SplittableRandom random;

  SolarSynthesiser() {
    random = new SplittableRandom(LocalDateTime.now().getNano());
  }

  private double[] computeDays(double hGlob, final int daysInMonth, double[] dailyHe0Hor, double he0HorSum) {
    double[] days = new double[daysInMonth];
    for (int d = 0; d < daysInMonth; d++) {
      days[d] = hGlob * (dailyHe0Hor[d] / he0HorSum);
    }
    return days;
  }

  private double computeE0OfDay(SolarDateTime day) {
    return Utils.EO_TAG * (1.0 - 0.0334 * Math.cos(0.0172 * day.getDayOfYear() - 0.04747));
  }

  private double[] computeKtOfHour(double kt, double phi, double[] sunYOfH) {
    double[] yOfh = new double[24];
    double[] ktOfh = new double[24];
    for (int h = 0; h < 24; h++) {
      if (sunYOfH[h] > 0.0) {
        double ysDeg = Utils.getRad(sunYOfH[h]);
        double ktm = -0.19 + 1.12 * kt + 0.24 * Math.exp(-8 * kt) + (0.32 - 1.6 * Math.pow(kt - 0.5, 2))
            * Math.exp((-0.19 - 2.27 * Math.pow(kt, 2) + 2.51 * Math.pow(kt, 3)) / Math.sin(ysDeg));
        double sigma = 0.14 * Math.exp(-20 * Math.pow(kt - 0.35, 2))
            * Math.exp(3 * (Math.pow(kt - 0.45, 2) + 16 * Math.pow(kt, 5)) * (1 - Math.sin(ysDeg)));
        double z = random.nextDouble();
        double r = sigma * Math.sqrt(1 - Math.pow(phi, 2))
            * ((Math.pow(z, 0.135) - Math.pow(1 - z, 0.135)) / 0.1975);
        if (h > 0) {
          yOfh[h] = phi * yOfh[h - 1] + r;
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

  double[] extractDays(SolarDateTime month, double hGlob, double lat, double lon) {
    final int daysInMonth = Utils.getDaysInMonth(month.getYear(), month.getMonth());
    if (hGlob <= 0) {
      return new double[daysInMonth];
    }
    double[] dailyHe0Hor = new double[daysInMonth];
    double he0HorSum = 0.0;
    for (int d = 0; d < daysInMonth; d++) {
      SolarPosition sunPos = new SolarPosition();
      double sumSinGammaS = 0.0;
      for (int h = 0; h < 24; h++) {
        SolarDateTime dt = new SolarDateTime(month.getYear(), month.getMonth(), d + 1, h);
        sunPos.compute(dt, lat, lon);
        if (sunPos.getYs() > 0) {
          sumSinGammaS += Utils.sin(sunPos.getYs());
        }
      }
      final double he0Hor = computeE0OfDay(month) * sumSinGammaS;
      dailyHe0Hor[d] = he0Hor;
      he0HorSum += he0Hor;
    }
    zeroGuard(he0HorSum);
    return computeDays(hGlob, daysInMonth, dailyHe0Hor, he0HorSum);
  }

  double[] extractHours(SolarDateTime day, double hGlob, double lat, double lon) {
    if (hGlob <= 0) {
      return new double[24];
    }
    SolarPosition sunPos = new SolarPosition();
    double[] sunYOfH = new double[24];
    double sumSinGammaS = 0.0;
    for (int h = 0; h < 24; h++) {
      SolarDateTime dt = new SolarDateTime(day.getYear(), day.getMonth(), day.getDayOfMonth(), h);
      sunPos.compute(dt, lat, lon);
      sunYOfH[h] = sunPos.getYs();
      if (sunPos.getYs() > 0) {
        sumSinGammaS += Utils.sin(sunPos.getYs());
      }
    }
    double he0Hor = computeE0OfDay(day) * sumSinGammaS;
    zeroGuard(he0Hor);
    double kt = hGlob / he0Hor;
    double phi1 = 0.38 + 0.06 * Math.cos(7.4 * kt - 2.5);
    return getBestHours(getValidHours(sunYOfH, he0Hor, kt, phi1));
  }

  private double[] getBestHours(Map<Double, double[]> validHours) {
    try {
      return validHours.get(Collections.min(validHours.keySet()));
    } catch (Exception e) {
      LOGGER.info(e.toString());
      return new double[24];
    }
  }

  private Map<Double, double[]> getValidHours(double[] sunYOfH, double hE0Hor, double kt, double phi) {
    int cnt = 0;
    double[] ktOfh;
    double[] egHorOfh;
    double hSynHor;
    Map<Double, double[]> hoursMap = new HashMap<>();
    do {
      boolean isNotAdd = false;
      ++cnt;
      ktOfh = computeKtOfHour(kt, phi, sunYOfH);
      egHorOfh = new double[24];
      hSynHor = 0.0;
      for (int h = 0; h < ktOfh.length; h++) { // NOSONAR
        double kth = ktOfh[h];
        if (kt == 0.0) {
          continue; // NOSONAR
        }
        double ktMax = 0.88 * Math.cos((Math.PI * (h + 1.0 - 12.5)) / 30.0);
        if (kth < 0.0 || kth > ktMax) {
          isNotAdd = true;
          break; // NOSONAR
        } else {
          egHorOfh[h] = ktOfh[h] * Utils.EO_TAG * Utils.sin(sunYOfH[h]);
          hSynHor += egHorOfh[h];
        }
      }
      if (!isNotAdd && kt > 0) {
        double diff = Math.abs((hSynHor / hE0Hor) - kt) / kt * 100.0;
        hoursMap.put(diff, egHorOfh);
      }
    } while (cnt < 10000);
    return hoursMap;
  }

  private void zeroGuard(double val) {
    if (val == 0) {
      throw new IllegalArgumentException("zero");
    }
  }
}
