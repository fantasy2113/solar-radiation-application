package de.josmer.app.lib.sun;

import java.time.LocalDateTime;
import java.util.Random;

public class SunToolBox {
    /// <summary>global for-loop start - 24 Hours</summary>
    public static final int MONTH_12 = 12;
    /// <summary>global for-loop start - 24 Hours</summary>
    public static final int FOR_START = 0;
    /// <summary>see Regenerative Energiesysteme [Quaschning] 2.6.2 ==> Perez-Modell</summary>
    public static final double K = 1.041;
    /// <summary>see Regenerative Energiesysteme [Quaschning] 2.6.2 ==> Perez-Modell</summary>
    public static final double Eo = 1360.8;
    public static final double EoTAG = 1367;
    /// <summary>convert degree to  radian RAD * [degree val]</summary>
    public static final double RAD = Math.PI / 180.0;
    /// <summary>global for-loop end [i smaller FOR_END] - 24 Hours</summary>
    private static final int FOR_END = 24;

    /// <summary>
    /// covert degree to radian
    /// </summary>
    /// <param name="angle">degree value</param>
    /// <returns>radian val</returns>
    public static double GetRad(double deg) {
        return deg * Math.PI / 180.0;
    }

    /// <summary>covert radian to degree</summary>
    /// <param name="angle">radian value</param>
    /// <returns>degree al</returns>
    public static double GetDeg(double rad) {
        return rad * 180.0 / Math.PI;
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="val"></param>
    /// <returns></returns>
    public static double Tan(double val) {
        return Math.tan(GetRad(val));
    }

    /// <summary>cos with degree to radian convert</summary>
    /// <param name="val">degree value</param>
    /// <returns>cos(degree to radian)=x</returns>
    public static double Cos(double val) {
        return Math.cos(GetRad(val));
    }

    /// <summary>sin with degree to radian convert</summary>
    /// <param name="val">degree value</param>
    /// <returns>sin(degree to radian)=x</returns>
    public static double Sin(double val) {
        return Math.sin(GetRad(val));
    }

    /// <summary>cos with degree to radian convert</summary>
    /// <param name="val">degree value</param>
    /// <returns>cos(degree to radian)=x</returns>
    public static double Cosh(double val) {
        return Math.cos(GetRad(val));
    }

    /// <summary>sin with degree to radian convert</summary>
    /// <param name="val">degree value</param>
    /// <returns>sin(degree to radian)=x</returns>
    public static double Sinh(double val) {
        return Math.sin(GetRad(val));
    }

    /// <summary>acos with degree to radian convert</summary>
    /// <param name="val"></param>
    /// <returns>Acos(degree to radian)=x</returns>
    public static double Acos(double val) {
        return Math.acos(GetRad(val));
    }

    /// <summary>Asin with degree to radian convert</summary>
    /// <param name="val"></param>
    /// <returns>Asin(degree to radian)=x</returns>
    public static double Asin(double val) {
        return Math.asin(GetRad(val));
    }

    /// <summary>getter for days in a specific month</summary>
    /// <param name="year">year</param>
    /// <param name="month">month number</param>
    /// <returns>days</returns>
    public static int GetDaysInMonth(int year, int month) {
        return LocalDateTime.of(year, month, 1, 0, 0, 0, 0).getMonth().getValue();
    }

    /// <summary>generate a double value</summary>
    /// <param name="one">from</param>
    /// <param name="two">to</param>
    /// <returns>double between one and two</returns>
    public static double GenRand(double one, double two) {
        Random rand = new Random(LocalDateTime.now().getNano());
        return one + rand.nextDouble() * (two - one);
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="kW"></param>
    /// <returns></returns>
    public static double GetKilowattToWatt(double kW) {
        return kW * 1000;
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="watt"></param>
    /// <returns></returns>
    public static double GetWattToKilowatt(double watt) {
        return watt / 1000;
    }
}
