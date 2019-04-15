package de.josmer.application.sun;

public class SunToolBox {
    /// <summary>global for-loop start - 24 Hours</summary>
    public const
    int MONTH_12 = 12;
    /// <summary>global for-loop start - 24 Hours</summary>
    public const
    int FOR_START = 0;
    /// <summary>global for-loop end [i smaller FOR_END] - 24 Hours</summary>
    public const
    int FOR_END = 24;
    /// <summary>see Regenerative Energiesysteme [Quaschning] 2.6.2 ==> Perez-Modell</summary>
    public const
    double K = 1.041;
    /// <summary>see Regenerative Energiesysteme [Quaschning] 2.6.2 ==> Perez-Modell</summary>
    public const
    double Eo = 1360.8;
    public const
    double EoTAG = 1367;
    /// <summary>convert degree to  radian RAD * [degree val]</summary>
    public const
    double RAD = Math.PI / 180.0;

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
        return Math.Tan(GetRad(val));
    }

    /// <summary>cos with degree to radian convert</summary>
    /// <param name="val">degree value</param>
    /// <returns>cos(degree to radian)=x</returns>
    public static double Cos(double val) {
        return Math.Cos(GetRad(val));
    }

    /// <summary>sin with degree to radian convert</summary>
    /// <param name="val">degree value</param>
    /// <returns>sin(degree to radian)=x</returns>
    public static double Sin(double val) {
        return Math.Sin(GetRad(val));
    }

    /// <summary>cos with degree to radian convert</summary>
    /// <param name="val">degree value</param>
    /// <returns>cos(degree to radian)=x</returns>
    public static double Cosh(double val) {
        return Math.Cos(GetRad(val));
    }

    /// <summary>sin with degree to radian convert</summary>
    /// <param name="val">degree value</param>
    /// <returns>sin(degree to radian)=x</returns>
    public static double Sinh(double val) {
        return Math.Sin(GetRad(val));
    }

    /// <summary>acos with degree to radian convert</summary>
    /// <param name="val"></param>
    /// <returns>acos(degree to radian)=x</returns>
    public static double Acos(double val) {
        return Math.Acos(GetRad(val));
    }

    /// <summary>asin with degree to radian convert</summary>
    /// <param name="val"></param>
    /// <returns>asin(degree to radian)=x</returns>
    public static double Asin(double val) {
        return Math.Asin(GetRad(val));
    }

    /// <summary>getter for days in a specific month</summary>
    /// <param name="year">year</param>
    /// <param name="month">month number</param>
    /// <returns>days</returns>
    public static int GetDaysInMonth(int year, int month) {
        return LocalDateTime.DaysInMonth(year, month);
    }

    /// <summary>generate a double value</summary>
    /// <param name="one">from</param>
    /// <param name="two">to</param>
    /// <returns>double between one and two</returns>
    public static double GenRand(double one, double two) {
        Random rand = new Random(LocalDateTime.Now.Millisecond);
        return one + rand.NextDouble() * (two - one);
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
