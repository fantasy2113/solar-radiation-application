package de.josmer.application.sun;

public class TAGModel {

    private const
    double E0 = 1367.0;
    private Random Rand;

    /// <summary>
    /// constructor
    /// </summary>
    public TAGModel() {
        Rand = new Random((int) (LocalDateTime.Now.Ticks % int.MaxValue));
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
            LocalDateTime dt = new LocalDateTime(day.Year, day.Month, day.Day, h, day.Minute, 0);
            sunPos.SunPositionDIN(dt, lat, lon, 1);
            sunYOfh[h] = sunPos.MYsAtmosphericRefractionCorrection();
            if (sunPos.MYsAtmosphericRefractionCorrection() > 0) {
                sumSinGammaS += Math.Sin(DegreeToRad(sunPos.MYsAtmosphericRefractionCorrection()));
            }
        }
        //double HE0Hor = E0 * sumSinGammaS;    // * 1h -> Wh/m2
        double HE0Hor = E0ofDay(day) * sumSinGammaS;    // * 1h -> Wh/m2
        double Kt = hGlob / HE0Hor;

        // formula 4.5
        double phi1 = 0.38 + 0.06 * Math.Cos(7.4 * Kt - 2.5);

        bool recalc = false;
        int cnt = 0;
        double[] KtOfh = null;
        double[] EgHorOfh = null;
        double KtSyn = 0.0; // for check: syntetic daily clearness index calculated back from hour values
        double HSynHor = 0.0;   // for check: daily syntetic energy should be similar to hGlob

        do {
            //System.out.println(string.Format("Calucatate try number {0} ...", ++cnt));
            ++cnt;
            // do the main calculation
            KtOfh = CalcKtOfh(lat, lon, Kt, phi1, sunYOfh);

            // check hour result
            recalc = false;
            EgHorOfh = new double[24];
            HSynHor = 0.0;
            KtSyn = 0.0;
            for (int h = 0; h < KtOfh.Length; h++) {
                double kt = KtOfh[h];
                if (kt == 0.0) {
                    continue;
                }

                // formula 4.11
                double ktMax = 0.88 * Math.Cos((Math.PI * (h + 1 - 12.5)) / 30.0);
                if (kt < 0.0) {
                    recalc = true;
                    break;
                } else if (kt > ktMax) {
                    recalc = true;
                    break;
                } else {
                    // formula 4.12
                    EgHorOfh[h] = KtOfh[h] * E0 * Math.Sin(DegreeToRad(sunYOfh[h]));
                    // summarize syntetic energy of hours
                    HSynHor += EgHorOfh[h]; // W/m2 * 1h => Wh/m2
                }
            }

            // check daily result
            if (!recalc) {
                // calculate syntetic clearness index of hour values
                KtSyn = HSynHor / HE0Hor;
                // check difference with cleaness index of daily irradiation is greater 3%
                double diff = Math.Abs(KtSyn - Kt) / Kt * 100.0;
                if (diff > 3.0) {
                    //System.out.println(string.Format("   -> Diff KtSyn={0:N2} from Kt={1:N2} : {2:N2}%", KtSyn, Kt, diff));
                    recalc = true;
                }
            }

        } while (recalc && (cnt <= 1000));

        // write result
        //System.out.println("RESULT:");
        //System.out.println(string.Format("Hglob      = {0:N1} Wh/m2", hGlob));
        //System.out.println(string.Format("Hhor       = {0:N1} Wh/m2", HSynHor));
        //System.out.println(string.Format("Kt         = {0:N3}", Kt));
        //System.out.println(string.Format("Kt (syn)   = {0:N3}", KtSyn));
        //System.out.println(string.Format("Loop count = {0}", cnt));
        //double sum = 0;
        //if (EgHorOfh != null)
        //{
        //    for (int h = 0; h < 24; h++) {
        //        //System.out.println(string.Format("EgHor[{0:00}] = {1:N1} W/m2", h, EgHorOfh[h]));
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
                double Ktm = -0.19 + 1.12 * Kt + 0.24 * Math.Exp(-8 * Kt)
                        + (0.32 - 1.6 * Math.Pow(Kt - 0.5, 2))
                        * Math.Exp((-0.19 - 2.27 * Math.Pow(Kt, 2) + 2.51 * Math.Pow(Kt, 3)) / Math.Sin(YsDeg));
                // formula 4.7
                double sigma = 0.14 * Math.Exp(-20 * Math.Pow(Kt - 0.35, 2)) * Math.Exp(3 * (Math.Pow(Kt - 0.45, 2) + 16 * Math.Pow(Kt, 5)) * (1 - Math.Sin(YsDeg)));

                // formula 4.8
                double z = Rand.NextDouble();
                double r = sigma * Math.Sqrt(1 - Math.Pow(phi1, 2)) * ((Math.Pow(z, 0.135) - Math.Pow(1 - z, 0.135)) / 0.1975);

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
        return E0 * (1 - 0.0334 * Math.Cos(0.0172 * (double) day.DayOfYear - 0.04747));
    }

    private static double DegreeToRad(double deg) {
        return deg * Math.PI / 180;
    }
}
