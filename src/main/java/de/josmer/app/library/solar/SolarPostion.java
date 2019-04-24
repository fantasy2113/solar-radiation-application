package de.josmer.app.library.solar;

import java.time.LocalDateTime;
import java.time.Year;

class SolarPostion {

    private static final double TIMEZONE = 1;
    private double ys;
    private double as;
    private double zenith;
    private LocalDateTime time;

    void calculate(LocalDateTime dt, double lat, double lon) {
        this.time = LocalDateTime.of(dt.getYear(), dt.getMonthValue(), dt.getDayOfMonth(), dt.getHour(), dt.getMinute(), 0, 0);
        double hour = this.time.getHour();
        double year = this.time.getYear();
        double min = this.time.getMinute();
        double sec = this.time.getSecond();
        double j2 = getDaysInYear((int) year);
        double j = this.time.getDayOfYear();
        double moz = hour + 1.0 / 60 * min + 1.0 / 3600 * sec - TIMEZONE + 1;
        moz = moz - 4 * (15 - lon) / 60;
        j = j * 360 / j2 + moz / 24;
        double decl = 0.3948 - 23.2559 * Math.cos(rad(j + 9.1)) - 0.3915 * Math.cos(rad(2 * j + 5.4)) - 0.1764 * Math.cos(rad(3 * j + 26.0));
        double zgl = 0.0066 + 7.3525 * Math.cos(rad(j + 85.9)) + 9.9359 * Math.cos(rad(2 * j + 108.9)) + 0.3387 * Math.cos(rad(3 * j + 105.2));
        double woz = moz + zgl / 60.0;
        double w = (12 - woz) * 15.0;
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
        this.as = sunaz;
        this.ys = sunhi;
        this.zenith = 90.0 - this.ys;
    }

    private double getDaysInYear(int year) {
        return Year.of(year).length();
    }

    private double rad(double grad) {
        return (grad * Utils.RAD);
    }

    private double grad(double rad) {
        return (rad * Utils.DEG);
    }

    double getSimpleDayAngle(int dayofyear, int year) {
        return (2.0 * Math.PI / (double) getDaysInYear(year)) * (dayofyear - 1);
    }

    double getAs() {
        return this.as;
    }

    double getYs() {
        return this.ys;
    }

    double getZenith() {
        return this.zenith;
    }
}
