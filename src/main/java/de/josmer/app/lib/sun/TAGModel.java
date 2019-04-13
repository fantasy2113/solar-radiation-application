package de.josmer.app.lib.sun;

import java.time.LocalDateTime;
import java.util.Random;

public class TAGModel {

    private static final double E0 = 1367.0;
    private Random Rand;

    /// <summary>
    /// constructor
    /// </summary>
    public TAGModel() {
        Rand = new Random(LocalDateTime.now().getNano());
    }

    private static double DegreeToRad(double deg) {
        return deg * Math.PI / 180;
    }

    /// <summary>
    /// calculate hourly irradiance values in W/m2 from daily energy in Wh/m2
    /// </summary>
    /// <param name="day">day to calculate</param>
    /// <param name="hGlob">daily global irradiance in Wh/m2</param>
    /// <param name="lat">latitude</param>
    /// <param name="lon">longitude</param>
    /// <returns>array of daily irradiation</returns>
    public double[] CalculateDay(LocalDateTime day, double hGlob, double lat, double lon) {
        SunPosition sunPos = new SunPosition();

        double[] sunYOfh = new double[24];

        // formula 4.3
        // init sun angle in degree
        double sumSinGammaS = 0.0;
        for (int h = 0; h < 24; h++) {
            LocalDateTime dt = LocalDateTime.of(day.getYear(), day.getMonthValue(), day.getDayOfMonth(), day.getHour(), day.getMinute());
            sunPos.SunPositionDIN(dt, lat, lon, 1);
            sunYOfh[h] = sunPos.MYsAtmosphericRefractionCorrection();
            if (sunPos.MYsAtmosphericRefractionCorrection() > 0) {
                sumSinGammaS += Math.sin(DegreeToRad(sunPos.MYsAtmosphericRefractionCorrection()));
            }
        }
        //double HE0Hor = E0 * sumSinGammaS;    // * 1h -> Wh/m2
        double HE0Hor = E0ofDay(day) * sumSinGammaS;    // * 1h -> Wh/m2
        double Kt = hGlob / HE0Hor;

        // formula 4.5
        double phi1 = 0.38 + 0.06 * Math.cos(7.4 * Kt - 2.5);

        boolean recalc = false;
        int cnt = 0;
        double[] KtOfh = null;
        double[] EgHorOfh = null;
        double KtSyn = 0.0; // for check: syntetic daily clearness index calculated back from hour values
        double HSynHor = 0.0;   // for check: daily syntetic energy should be similar to hGlob

        do {
            //Console.WriteLine(string.Format("Calucatate try number {0} ...", ++cnt));
            ++cnt;
            // do the main calculation
            KtOfh = CalcKtOfh(lat, lon, Kt, phi1, sunYOfh);

            // check hour result
            recalc = false;
            EgHorOfh = new double[24];
            HSynHor = 0.0;
            KtSyn = 0.0;
            for (int h = 0; h < KtOfh.length; h++) {
                double kt = KtOfh[h];
                if (kt == 0.0) {
                    continue;
                }

                // formula 4.11
                double ktMax = 0.88 * Math.cos((Math.PI * (h + 1 - 12.5)) / 30.0);
                if (kt < 0.0) {
                    recalc = true;
                    break;
                } else if (kt > ktMax) {
                    recalc = true;
                    break;
                } else {
                    // formula 4.12
                    EgHorOfh[h] = KtOfh[h] * E0 * Math.sin(DegreeToRad(sunYOfh[h]));
                    // summarize syntetic energy of hours
                    HSynHor += EgHorOfh[h]; // W/m2 * 1h => Wh/m2
                }
            }

            // check daily result
            if (!recalc) {
                // calculate syntetic clearness index of hour values
                KtSyn = HSynHor / HE0Hor;
                // check difference with cleaness index of daily irradiation is greater 3%
                double diff = Math.abs(KtSyn - Kt) / Kt * 100.0;
                if (diff > 3.0) {
                    //Console.WriteLine(string.Format("   -> Diff KtSyn={0:N2} from Kt={1:N2} : {2:N2}%", KtSyn, Kt, diff));
                    recalc = true;
                }
            }

        } while (recalc && (cnt <= 1000));

        // write result
        //Console.WriteLine("RESULT:");
        //Console.WriteLine(string.Format("Hglob      = {0:N1} Wh/m2", hGlob));
        //Console.WriteLine(string.Format("Hhor       = {0:N1} Wh/m2", HSynHor));
        //Console.WriteLine(string.Format("Kt         = {0:N3}", Kt));
        //Console.WriteLine(string.Format("Kt (syn)   = {0:N3}", KtSyn));
        //Console.WriteLine(string.Format("Loop count = {0}", cnt));
        //double sum = 0;
        //if (EgHorOfh != null)
        //{
        //    for (int h = 0; h < 24; h++) {
        //        //Console.WriteLine(string.Format("EgHor[{0:00}] = {1:N1} W/m2", h, EgHorOfh[h]));
        //        sum+=EgHorOfh[h];
        //    }
        //}
        //Console.ReadLine();

        return EgHorOfh;
    }

    /// <summary>
    /// calculate hourly cleaness index
    /// </summary>
    /// <param name="lat">latitude</param>
    /// <param name="lon">longitude</param>
    /// <param name="Kt">daily clearness index</param>
    /// <param name="phi1">auto correlation</param>
    /// <param name="sunYOfh">sun degree of hour</param>
    /// <returns></returns>
    private double[] CalcKtOfh(double lat, double lon, double Kt, double phi1, double[] sunYOfh) {
        double[] yOfh = new double[24];
        double[] KtOfh = new double[24];

        for (int h = 0; h < 24; h++) {
            if (sunYOfh[h] > 0.0) {
                // formula 4.6
                double YsDeg = DegreeToRad(sunYOfh[h]);
                double Ktm = -0.19 + 1.12 * Kt + 0.24 * Math.exp(-8 * Kt)
                        + (0.32 - 1.6 * Math.pow(Kt - 0.5, 2))
                        * Math.exp((-0.19 - 2.27 * Math.pow(Kt, 2) + 2.51 * Math.pow(Kt, 3)) / Math.sin(YsDeg));
                // formula 4.7
                double sigma = 0.14 * Math.exp(-20 * Math.pow(Kt - 0.35, 2)) * Math.exp(3 * (Math.pow(Kt - 0.45, 2) + 16 * Math.pow(Kt, 5)) * (1 - Math.sin(YsDeg)));

                // formula 4.8
                double z = Rand.nextDouble();
                double r = sigma * Math.sqrt(1 - Math.pow(phi1, 2)) * ((Math.pow(z, 0.135) - Math.pow(1 - z, 0.135)) / 0.1975);

                // formula 4.9
                if (h > 0) {
                    yOfh[h] = phi1 * yOfh[h - 1] + r;
                } else {
                    // shoud not occure because of Ys <= 0 at midnight
                    yOfh[h] = r;
                }

                // formula 4.10
                KtOfh[h] = Ktm + sigma * yOfh[h];
            } else {
                KtOfh[h] = 0.0;
            }
        }
        return KtOfh;
    }

    private double E0ofDay(LocalDateTime day) {
        // formula 4.4
        return E0 * (1 - 0.0334 * Math.cos(0.0172 * (double) day.getDayOfYear() - 0.04747));
    }
}
