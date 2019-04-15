package de.josmer.application.sun;

import java.util.HashMap;
import java.util.Map;

public class Irradiation {

    private final String F_11 = "F11";
    private final String F_12 = "F12";
    private final String F_13 = "F13";
    private final String F_21 = "F21";
    private final String F_22 = "F22";
    private final String F_23 = "F23";
    private SunPosition MSunPos;
    private double MEDiffGen;
    private double MEDirGen;
    private double MEReflGen;
    private double MEGlobalGenEffective;
    private double MEGlobalGen;
    private static Map<String, double[]> FTabelle;
    private double Albedo = 0;
    private double Ae = 0;
    private double Ye = 0;
    private double Himmelsklarheitsindex = 0;
    private double Delta = 0;
    private double AirMass = 0;
    private double AoiProjection = 0;
    private double F1 = 0;
    private double F2 = 0;
    private double F11 = 0;
    private double F12 = 0;
    private double F13 = 0;
    private double F21 = 0;
    private double F22 = 0;
    private double F23 = 0;
    private double A = 0;
    private double B = 0;
    private double Longitude = 0;
    private double Latitude = 0;
    private double Aoi = 0;
    private double ProjectionRatio = 0;
    private double EDirHorExtra = 0;
    private double AnisotropyIndex = 0;

    public Irradiation(double Ye, double Ae, double Latitude, double Longitude, double Albedo) {
        this.Ye = Ye;
        this.Ae = Ae;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Albedo = Albedo;
        MSunPos = new SunPosition();
        InitFTabelle();
    }

    /// <summary>
    /// http://files.pvsyst.com/help/iam_loss.htm
    /// </summary>
    /// <param name="bo"></param>
    /// <returns></returns>
    private double GetIAM(double bo =0.05) {
        double retVal = 1 - bo * (1 / SunToolBox.Cos(Aoi - 1));
        if (Math.Abs(Aoi) >= 90) {
            retVal = 0;
        }
        retVal = Math.Max(0, retVal);
        return retVal;
    }

    /// <summary>
    /// https://github.com/pvlib/pvlib-python/blob/master/pvlib/pvsystem.py#L2118
    /// </summary>
    /// <param name="percent"></param>
    /// <returns></returns>
    private double GetShading(double shading =5) {
        if (shading < 0) {
            return 1;
        } else if (shading > 10) {
            return 0.9;
        } else {
            return (100 - shading) / 100;
        }
    }

    private void InitFTabelle() {
        FTabelle = new HashMap<>();
        FTabelle.put(F_11, new double[]{-0.008, 0.130, 0.330, 0.568, 0.873, 1.132, 1.060, 0.678});
        FTabelle.put(F_12, new double[]{0.588, 0.683, 0.487, 0.187, -0.392, -1.237, -1.600, -0.327});
        FTabelle.put(F_13, new double[]{-0.062, -0.151, -0.221, -0.295, -0.362, -0.412, -0.359, -0.250});
        FTabelle.put(F_21, new double[]{-0.060, -0.019, 0.055, 0.109, 0.226, 0.288, 0.264, 0.156});
        FTabelle.put(F_22, new double[]{0.072, 0.066, -0.064, -0.152, -0.462, -0.823, -1.127, -1.377});
        FTabelle.put(F_23, new double[]{-0.022, -0.029, -0.026, -0.014, 0.001, 0.056, 0.131, 0.251});
    }

    private void SetIndexFTabelle() {
        if (Himmelsklarheitsindex >= 1.0 && Himmelsklarheitsindex < 1.065) {
            SetFValues(0);
        } else if (Himmelsklarheitsindex >= 1.065 && Himmelsklarheitsindex < 1.230) {
            SetFValues(1);
        } else if (Himmelsklarheitsindex >= 1.23 && Himmelsklarheitsindex < 1.5) {
            SetFValues(2);
        } else if (Himmelsklarheitsindex >= 1.5 && Himmelsklarheitsindex < 1.95) {
            SetFValues(3);
        } else if (Himmelsklarheitsindex >= 1.95 && Himmelsklarheitsindex < 2.8) {
            SetFValues(4);
        } else if (Himmelsklarheitsindex >= 2.8 && Himmelsklarheitsindex < 4.5) {
            SetFValues(5);
        } else if (Himmelsklarheitsindex >= 4.5 && Himmelsklarheitsindex < 6.2) {
            SetFValues(6);
        } else if (Himmelsklarheitsindex >= 6.2) {
            SetFValues(7);
        }
    }

    private void SetFValues(int index) {
        F11 = FTabelle.get(F_11)[index];
        F12 = FTabelle.get(F_12)[index];
        F13 = FTabelle.get(F_13)[index];
        F21 = FTabelle.get(F_21)[index];
        F22 = FTabelle.get(F_22)[index];
        F23 = FTabelle.get(F_23)[index];
    }

    private void SetProjectionRatio() {
        double cosTT = AoiProjection;
        double cosSolarZenith = SunToolBox.Cos(MSunPos.MZenithAtmosphericRefractionCorrection());
        ProjectionRatio = cosTT / cosSolarZenith;
    }

    private void SetAnisotropyIndex(double EDiffHor, double EDirHor, double EGlobalHor) {
        AnisotropyIndex = EDirHor / EDirHorExtra;
    }

    /// <summary>
    /// https://github.com/pvlib/pvlib-python/blob/master/pvlib/solarposition.py#L847
    /// https://github.com/pvlib/pvlib-python/blob/master/pvlib/irradiance.py#L125
    /// </summary>
    /// <param name="Dt"></param>
    private void SetEDirHorExtra(LocalDateTime Dt) {
        double b = MSunPos.CalculateSimpleDayAngle(Dt.DayOfYear, Dt.Year);
        double RoverR0sqrd = (1.00011 + 0.034221 * Math.Cos(b) + 0.00128 * Math.Sin(b) + 0.000719 * Math.Cos(2 * b) + 7.7e-05 * Math.Sin(2 * b));
        EDirHorExtra = SunToolBox.Eo * RoverR0sqrd;
    }

    /// <summary>
    /// https://github.com/pvlib/pvlib-python/blob/master/pvlib/irradiance.py#L1024
    /// </summary>
    private void SetF1AndF2() {
        double z = SunToolBox.GetRad(MSunPos.MZenithAtmosphericRefractionCorrection());
        F1 = (F11 + F12 * Delta + F13 * z);
        F1 = Math.Max(F1, 0);
        F2 = (F21 + F22 * Delta + F23 * z);
        F2 = Math.Max(F2, 0);
    }

    /// <summary>
    /// https://github.com/pvlib/pvlib-python/blob/master/pvlib/irradiance.py#L145
    /// https://github.com/pvlib/pvlib-python/blob/master/pvlib/irradiance.py#L182
    /// </summary>
    private void SetAoi() {
        AoiProjection = SunToolBox.Cos(Ye) * SunToolBox.Cos(MSunPos.MZenithAtmosphericRefractionCorrection()) + SunToolBox.Sin(Ye) * SunToolBox.Sin(MSunPos.MZenithAtmosphericRefractionCorrection()) * SunToolBox.Cos(MSunPos.MAs - Ae);
        Aoi = SunToolBox.GetDeg(Math.Acos(AoiProjection));
    }

    /// <summary>
    /// https://github.com/pvlib/pvlib-python/blob/master/pvlib/atmosphere.py#L213
    /// </summary>
    private void SetAirMass() {
        //AirMass = 1.0 / SunToolBox.cos(MSunPos.zenith);
        AirMass = (1.0 / (SunToolBox.Cos(MSunPos.MZenithAtmosphericRefractionCorrection()) + 0.50572 * ((Math.Pow((6.07995 + (90 - MSunPos.MZenithAtmosphericRefractionCorrection())), -1.6364)))));
    }

    /// <summary>
    /// https://github.com/pvlib/pvlib-python/blob/master/pvlib/irradiance.py#L988
    /// </summary>
    /// <param name="EdiffHor"></param>
    /// <param name="Dt"></param>
    private void SetDelta(double EDiffHor, double EDirHor, double EGlobalHor, LocalDateTime Dt) {
        Delta = EDiffHor * AirMass / EDirHorExtra;
    }

    /// <summary>
    /// https://github.com/pvlib/pvlib-python/blob/master/pvlib/irradiance.py#L1030
    /// </summary>
    private void SetAandB() {
        A = AoiProjection;
        A = Math.Max(A, 0);
        B = SunToolBox.Cos(MSunPos.MZenithAtmosphericRefractionCorrection());
        B = Math.Max(B, SunToolBox.Cos(85));
    }

    /// <summary>
    /// https://github.com/pvlib/pvlib-python/blob/master/pvlib/irradiance.py#L444
    /// </summary>
    /// <param name="EGlobalHor"></param>
    private void SetMEReflGen(double EDiffHor, double EDirHor, double EGlobalHor) {
        MEReflGen = EGlobalHor * Albedo * (1 - SunToolBox.Cos(Ye)) * 0.5;
    }

    /// <summary>
    /// https://github.com/pvlib/pvlib-python/blob/master/pvlib/irradiance.py#L218
    /// </summary>
    /// <param name="EDirHor"></param>
    private void SetMEDirGen(double EDiffHor, double EDirHor, double EGlobalHor) {
        double cosSolarZenith = SunToolBox.Cos(MSunPos.MZenithAtmosphericRefractionCorrection());
        double ratio = AoiProjection / cosSolarZenith;
        MEDirGen = EDirHor * ratio;
        MEDirGen = Math.Max(MEDirGen, 0);
    }

    /// <summary>
    /// https://github.com/pvlib/pvlib-python/blob/master/pvlib/irradiance.py#L991
    /// </summary>
    /// <param name="EDiffHor"></param>
    /// <param name="EDirHor"></param>
    private void SetHimmelsklarheitsindex(double EDiffHor, double EDirHor, double EGlobalHor) {
        double z = SunToolBox.GetRad(MSunPos.MZenithAtmosphericRefractionCorrection());
        Himmelsklarheitsindex = ((EDiffHor + EDirHor) / EDiffHor + SunToolBox.K * Math.Pow(z, 3)) / (1 + SunToolBox.K * Math.Pow(z, 3));
    }

    /// <summary>
    /// https://github.com/pvlib/pvlib-python/blob/master/pvlib/irradiance.py#L1038
    /// </summary>
    /// <param name="EDiffHor"></param>
    private void SetMEDiffGenPerez(double EDiffHor, double EDirHor, double EGlobalHor) {
        double term1 = 0.5 * (1 - F1) * (1 + SunToolBox.Cos(Ye));
        double term2 = F1 * A / B;
        double term3 = F2 * SunToolBox.Sin(Ye);
        MEDiffGen = Math.Max(0, EDiffHor * (term1 + term2 + term3));
    }

    public void SetMEDiffGenHayDavies(double EDiffHor, double EDirHor, double EGlobalHor) {
        double term1 = 1 - AnisotropyIndex;
        double term2 = 0.5 * (1 + SunToolBox.Cos(Ye));
        MEDiffGen = EDiffHor * (AnisotropyIndex * ProjectionRatio + term1 * term2);
        MEDiffGen = Math.Max(MEDiffGen, 0);
    }

    public void CalculateHour(double EDiffHor, double EDirHor, double EGlobalHor, LocalDateTime Dt, int DiffModel, double shading) {
        LocalDateTime dateTime = new LocalDateTime(Dt.Year, Dt.Month, Dt.Day, Dt.Hour, Dt.Minute, 0);
        MSunPos.SunPositionDIN(dateTime, Latitude, Longitude, 1);
        if (MSunPos.MYsAtmosphericRefractionCorrection() > 0 && EDiffHor > 0 && EDirHor > 0 && EGlobalHor > 0) {
            SetAoi();
            SetEDirHorExtra(Dt);
            SetMEDirGen(EDiffHor, EDirHor, EGlobalHor);
            SetMEReflGen(EDiffHor, EDirHor, EGlobalHor);
            switch (DiffModel) {
                case 1:
                    SetAirMass();
                    SetDelta(EDiffHor, EDirHor, EGlobalHor, Dt);
                    SetHimmelsklarheitsindex(EDiffHor, EDirHor, EGlobalHor);
                    SetIndexFTabelle();
                    SetF1AndF2();
                    SetAandB();
                    SetMEDiffGenPerez(EDiffHor, EDirHor, EGlobalHor);
                    break;
                case 2:
                    SetProjectionRatio();
                    SetAnisotropyIndex(EDiffHor, EDirHor, EGlobalHor);
                    SetMEDiffGenHayDavies(EDiffHor, EDirHor, EGlobalHor);
                    break;
                default:
                    MEDiffGen = 0;
                    break;
            }
            MEGlobalGen = MEDiffGen + MEDirGen + MEReflGen;
            MEGlobalGenEffective = (MEDiffGen + (MEDirGen * GetIAM()) + MEReflGen) * GetShading(shading);
        }
    }

}
