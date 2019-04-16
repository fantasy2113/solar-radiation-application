package de.josmer.application.library.sun;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.Year;

class SunPostion {
    private static final Logger LOGGER = LoggerFactory.getLogger(SunPostion.class.getName());
    private static final double TIMEZONE = 1;
    private double ys;
    private double as;
    private double zenith;
    private double[] monthYearCelsiusArr;
    private double[] monthYearHpaArr;
    private LocalDateTime time;

    SunPostion() {
        this.monthYearCelsiusArr = new double[]{0.4, 1.1, 4.6, 8.6, 13.8, 16.4, 18.3, 17.9, 13.7, 9.1, 4.3, 1.7};
        this.monthYearHpaArr = new double[]{1017, 1017.1, 1015.1, 1013.8, 1015.5, 1015.1, 1015.4, 1016.0, 1016.2, 1016.6, 1015.6, 1015.5};
    }

    void calculate(LocalDateTime dt, double lat, double lon) {
        time = LocalDateTime.of(dt.getYear(), dt.getMonthValue(), dt.getDayOfMonth(), dt.getHour(), dt.getMinute(), 0, 0);
        double hour = time.getHour();
        double year = time.getYear();
        double min = time.getMinute();
        double sec = time.getSecond();
        double j2 = getDaysInYear((int) year);
        double j = time.getDayOfYear();
        double moz = hour + 1.0 / 60 * min + 1.0 / 3600 * sec - TIMEZONE + 1;
        moz = moz - 4 * (15 - lon) / 60;
        j = j * 360 / j2 + moz / 24;
        double decl = 0.3948 - 23.2559 * Math.cos(rad(j + 9.1)) - 0.3915 * Math.cos(rad(2 * j + 5.4)) - 0.1764 * Math.cos(rad(3 * j + 26.0));
        double zgl = 0.0066 + 7.3525 * Math.cos(rad(j + 85.9)) + 9.9359 * Math.cos(rad(2 * j + 108.9)) + 0.3387 * Math.cos(rad(3 * j + 105.2));
        double woz = moz + zgl / 60;
        double w = (12 - woz) * 15;
        double asinGs = Math.cos(rad(w)) * Math.cos(rad(lat)) * Math.cos(rad(decl)) + Math.sin(rad(lat)) * Math.sin(rad(decl));
        if (asinGs > 1) {
            asinGs = 1;
        }
        if (asinGs < -1) {
            asinGs = -1;
        }
        double sunhi = grad(Math.asin(asinGs));
        double acosAs = (Math.sin(rad(sunhi)) * Math.sin(rad(lat)) - Math.sin(rad(decl))) / (Math.cos(rad(sunhi)) * Math.cos(rad(lat)));
        if (acosAs > 1) {
            acosAs = 1;
        }
        if (acosAs < -1) {
            acosAs = -1;
        }
        double sunaz = grad(Math.acos(acosAs));
        if ((woz > 12) || (woz < 0)) {
            sunaz = 180 + sunaz;
        } else {
            sunaz = 180 - sunaz;
        }
        as = sunaz;
        ys = sunhi;
        zenith = 90.0 - ys;
    }

    private double getDaysInYear(int year) {
        return Year.of(year).length();
    }

    private double rad(double grad) {
        return (grad * Math.PI / 180);
    }

    private double grad(double rad) {
        return (rad * 180 / Math.PI);
    }

    private double getAtmosphericRefractionCorrection(double localPressure, double localTemp) {
        int isSwitch = ys >= -1.0 * (0.26667 + 0.5667) ? 1 : 0;
        return ((localPressure / 1010.0) * (283.0 / (273 + localTemp)) * 1.02 / (60 * Calc.tan((ys + 10.3 / (ys + 5.11))))) * isSwitch;
    }

    double getYsCorr() {
        // https://github.com/pvlib/pvlib-python/blob/master/pvlib/test/test_spa.py#L40 0.5667
        try {
            int index = time.getMonthValue() - 1;
            double hpa = monthYearHpaArr[index];
            double celsius = monthYearCelsiusArr[index];
            return this.ys + getAtmosphericRefractionCorrection(hpa, celsius);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return 0;
    }

    double getZenithCorr() {
        return 90 - getYsCorr();
    }

    double getSimpleDayAngle(int dayofyear, int year) {
        return (2.0 * Math.PI / getDaysInYear(year)) * (dayofyear - 1);
    }

    double getAs() {
        return as;
    }

    double getYs() {
        return ys;
    }

    double getZenith() {
        return zenith;
    }
}
