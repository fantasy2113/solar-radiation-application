package de.josmer.app.lib.sun;

import java.time.LocalDateTime;

public class SunPosition {
    /// <summary>see Regenerative Energiesysteme [Quaschning] 2.5 ==> Picture 2.16</summary>
    public double MYs /// <summary>see Regenerative Energiesysteme [Quaschning] 2.5 ==> Picture 2.16</summary>
    public double MAs
    public double MZenith
    double[] MonthYearCelsiusArr = new double[]{0.4, 1.1, 4.6, 8.6, 13.8, 16.4, 18.3, 17.9, 13.7, 9.1, 4.3, 1.7};
    double[] MonthYearHpaArr = new double[]{1017, 1017.1, 1015.1, 1013.8, 1015.5, 1015.1, 1015.4, 1016.0, 1016.2, 1016.6, 1015.6, 1015.5};
    private LocalDateTime Time;

    {
        get;
        private set
    }

    private static readonly

    {
        get;
        private set
    }

    private static readonly

    {
        get;
        private set
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="dt"></param>
    /// <param name="lat"></param>
    /// <param name="lon"></param>
    /// <param name="timezone"></param>
    public void SunPositionDIN(LocalDateTime dt, double lat, double lon, double timezone) {
        Time = new LocalDateTime(dt.Year, dt.Month, dt.Day, dt.Hour, dt.Minute, 0, 0);
        double hour = Time.Hour;
        double year = Time.Year;
        double min = Time.Minute;
        double sec = Time.Second;
        double J2 = GetDaysInYear((int) year);
        double J = Time.DayOfYear;
        double MOZ = hour + 1.0 / 60 * min + 1.0 / 3600 * sec - timezone + 1;
        MOZ = MOZ - 4 * (15 - lon) / 60;
        J = J * 360 / J2 + MOZ / 24;
        double decl = 0.3948 - 23.2559 * Math.cos(rad(J + 9.1)) - 0.3915 * Math.cos(rad(2 * J + 5.4)) - 0.1764 * Math.cos(rad(3 * J + 26.0));
        double Zgl = 0.0066 + 7.3525 * Math.cos(rad(J + 85.9)) + 9.9359 * Math.cos(rad(2 * J + 108.9)) + 0.3387 * Math.cos(rad(3 * J + 105.2));
        double WOZ = MOZ + Zgl / 60;
        double w = (12 - WOZ) * 15;
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
        if ((WOZ > 12) || (WOZ < 0)) {
            sunaz = 180 + sunaz;
        } else {
            sunaz = 180 - sunaz;
        }
        MAs = sunaz;
        MYs = sunhi;
        MZenith = 90.0 - MYs;
    }

    /// <summary></summary>
    /// <param name="year"></param>
    /// <returns></returns>
    private double GetDaysInYear(int year) {
        LocalDateTime thisYear = new LocalDateTime(year, 1, 1);
        LocalDateTime nextYear = new LocalDateTime(year + 1, 1, 1);
        return (nextYear - thisYear).Days;
    }

    private double rad(double grad) {
        return (grad * Math.PI / 180);
    }

    private double grad(double rad) {
        return (rad * 180 / Math.PI);
    }

    private double GetAtmosphericRefractionCorrection(double localPressure, double localTemp, double atmosRefract) {
        bool isSwitch = MYs >= -1.0 * (0.26667 + atmosRefract);
        double deltaYs = ((localPressure / 1010.0) * (283.0 / (273 + localTemp)) * 1.02 / (60 * SunToolBox.Tan((MYs + 10.3 / (MYs + 5.11))))) * Convert.ToInt32(isSwitch);
        return deltaYs;
    }

    public double MYsAtmosphericRefractionCorrection() {
        // https://github.com/pvlib/pvlib-python/blob/master/pvlib/test/test_spa.py#L40 0.5667
        double ys = 0;
        int index;
        double hpa;
        double celsius;
        try {
            index = Time.Month - 1;
            hpa = MonthYearHpaArr[index];
            celsius = MonthYearCelsiusArr[index];
            ys = MYs + GetAtmosphericRefractionCorrection(hpa, celsius, 0.5667);
        } catch (Exception) {
            throw
        }
        return ys;
    }

    public double MZenithAtmosphericRefractionCorrection() {
        double zenith = 90 - MYsAtmosphericRefractionCorrection();
        return zenith;
    }

    /// <summary>
    /// https://github.com/pvlib/pvlib-python/blob/master/pvlib/solarposition.py#L847
    /// </summary>
    /// <param name="dayofyear"></param>
    /// <param name="year"></param>
    /// <returns></returns>
    public double CalculateSimpleDayAngle(int dayofyear, int year) {
        return (2.0 * Math.PI / GetDaysInYear(year)) * (dayofyear - 1);
    }
}
