package de.josmer.application.library.sun;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

class Radiation {

    private static Map<String, double[]> FTabelle;
    private final String F_11 = "F11";
    private final String F_12 = "F12";
    private final String F_13 = "F13";
    private final String F_21 = "F21";
    private final String F_22 = "F22";
    private final String F_23 = "F23";
    private SubPostion MSunPos;
    private double MEDiffGen;
    private double MEDirGen;
    private double MEReflGen;
    private double MEGlobalGenEffective;
    private double MEGlobalGen;
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

    public Radiation(double Ye, double Ae, double Latitude, double Longitude, double Albedo) {
        this.Ye = Ye;
        this.Ae = Ae;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Albedo = Albedo;
        MSunPos = new SubPostion();
        InitFTabelle();
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

    public static void setFTabelle(Map<String, double[]> FTabelle) {
        Radiation.FTabelle = FTabelle;
    }

    private void SetAnisotropyIndex(double EDiffHor, double EDirHor, double EGlobalHor) {
        AnisotropyIndex = EDirHor / EDirHorExtra;
    }

    private void SetProjectionRatio() {
        double cosTT = AoiProjection;
        double cosSolarZenith = Calc.cos(MSunPos.GetZenithAtmosphericRefractionCorrection());
        ProjectionRatio = cosTT / cosSolarZenith;
    }

    /// <summary>
    /// https://github.com/pvlib/pvlib-python/blob/master/pvlib/irradiance.py#L1024
    /// </summary>
    private void SetF1AndF2() {
        double z = Calc.getRad(MSunPos.GetZenithAtmosphericRefractionCorrection());
        F1 = (F11 + F12 * Delta + F13 * z);
        F1 = Math.max(F1, 0);
        F2 = (F21 + F22 * Delta + F23 * z);
        F2 = Math.max(F2, 0);
    }

    /// <summary>
    /// https://github.com/pvlib/pvlib-python/blob/master/pvlib/irradiance.py#L145
    /// https://github.com/pvlib/pvlib-python/blob/master/pvlib/irradiance.py#L182
    /// </summary>
    private void SetAoi() {
        AoiProjection = Calc.cos(Ye) * Calc.cos(MSunPos.GetZenithAtmosphericRefractionCorrection()) + Calc.sin(Ye) * Calc.sin(MSunPos.GetZenithAtmosphericRefractionCorrection()) * Calc.cos(MSunPos.as - Ae);
        Aoi = Calc.getDeg(Math.acos(AoiProjection));
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
    /// https://github.com/pvlib/pvlib-python/blob/master/pvlib/atmosphere.py#L213
    /// </summary>
    private void SetAirMass() {
        //AirMass = 1.0 / Calc.cos(MSunPos.zenith);
        AirMass = (1.0 / (Calc.cos(MSunPos.GetZenithAtmosphericRefractionCorrection()) + 0.50572 * ((Math.pow((6.07995 + (90 - MSunPos.GetZenithAtmosphericRefractionCorrection())), -1.6364)))));
    }

    /// <summary>
    /// https://github.com/pvlib/pvlib-python/blob/master/pvlib/irradiance.py#L1030
    /// </summary>
    private void SetAandB() {
        A = AoiProjection;
        A = Math.max(A, 0);
        B = Calc.cos(MSunPos.GetZenithAtmosphericRefractionCorrection());
        B = Math.max(B, Calc.cos(85));
    }

    /// <summary>
    /// https://github.com/pvlib/pvlib-python/blob/master/pvlib/irradiance.py#L444
    /// </summary>
    /// <param name="EGlobalHor"></param>
    private void SetMEReflGen(double EDiffHor, double EDirHor, double EGlobalHor) {
        MEReflGen = EGlobalHor * Albedo * (1.0 - Calc.cos(Ye)) * 0.5;
    }

    /// <summary>
    /// https://github.com/pvlib/pvlib-python/blob/master/pvlib/irradiance.py#L218
    /// </summary>
    /// <param name="EDirHor"></param>
    private void SetMEDirGen(double EDiffHor, double EDirHor, double EGlobalHor) {
        double cosSolarZenith = Calc.cos(MSunPos.GetZenithAtmosphericRefractionCorrection());
        double ratio = AoiProjection / cosSolarZenith;
        MEDirGen = EDirHor * ratio;
        MEDirGen = Math.max(MEDirGen, 0);
    }

    /// <summary>
    /// https://github.com/pvlib/pvlib-python/blob/master/pvlib/irradiance.py#L991
    /// </summary>
    /// <param name="EDiffHor"></param>
    /// <param name="EDirHor"></param>
    private void SetHimmelsklarheitsindex(double EDiffHor, double EDirHor, double EGlobalHor) {
        double z = Calc.getRad(MSunPos.GetZenithAtmosphericRefractionCorrection());
        Himmelsklarheitsindex = ((EDiffHor + EDirHor) / EDiffHor + Calc.K * Math.pow(z, 3)) / (1 + Calc.K * Math.pow(z, 3));
    }

    /// <summary>
    /// https://github.com/pvlib/pvlib-python/blob/master/pvlib/irradiance.py#L1038
    /// </summary>
    /// <param name="EDiffHor"></param>
    private void SetMEDiffGenPerez(double EDiffHor, double EDirHor, double EGlobalHor) {
        double term1 = 0.5 * (1 - F1) * (1 + Calc.cos(Ye));
        double term2 = F1 * A / B;
        double term3 = F2 * Calc.sin(Ye);
        MEDiffGen = Math.max(0, EDiffHor * (term1 + term2 + term3));
    }

    public void SetMEDiffGenHayDavies(double EDiffHor, double EDirHor, double EGlobalHor) {
        double term1 = 1 - AnisotropyIndex;
        double term2 = 0.5 * (1 + Calc.cos(Ye));
        MEDiffGen = EDiffHor * (AnisotropyIndex * ProjectionRatio + term1 * term2);
        MEDiffGen = Math.max(MEDiffGen, 0);
    }

    /// <summary>
    /// https://github.com/pvlib/pvlib-python/blob/master/pvlib/solarposition.py#L847
    /// https://github.com/pvlib/pvlib-python/blob/master/pvlib/irradiance.py#L125
    /// </summary>
    /// <param name="Dt"></param>
    private void SetEDirHorExtra(LocalDateTime Dt) {
        double b = MSunPos.getSimpleDayAngle(Dt.getDayOfYear(), Dt.getYear());
        double RoverR0sqrd = (1.00011 + 0.034221 * Math.cos(b) + 0.00128 * Math.sin(b) + 0.000719 * Math.cos(2 * b) + 7.7e-05 * Math.sin(2 * b));
        EDirHorExtra = Calc.EO * RoverR0sqrd;
    }

    private double getDiffGlobHor(double val) {
        final double kt = val / Calc.EO * Calc.sin(MSunPos.getYsAtmosphericRefractionCorrection());
        if (kt <= 3.0) {

        } else if (kt < 0.78) {

        }
        return 0;
    }

    public void CalculateHour(double EDiffHor, double EDirHor, double EGlobalHor, LocalDateTime Dt, int DiffModel) {
        LocalDateTime dateTime = LocalDateTime.of(Dt.getYear(), Dt.getMonthValue(), Dt.getDayOfMonth(), Dt.getHour(), Dt.getMinute(), 0, 0);
        MSunPos.calculate(dateTime, Latitude, Longitude, 1);
        if (MSunPos.getYsAtmosphericRefractionCorrection() > 0 && EDiffHor > 0 && EDirHor > 0 && EGlobalHor > 0) {
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
        }
    }

    public String getF_11() {
        return F_11;
    }

    public String getF_12() {
        return F_12;
    }

    public String getF_13() {
        return F_13;
    }

    public String getF_21() {
        return F_21;
    }

    public String getF_22() {
        return F_22;
    }

    public String getF_23() {
        return F_23;
    }

    public SubPostion getMSunPos() {
        return MSunPos;
    }

    public void setMSunPos(SubPostion MSunPos) {
        this.MSunPos = MSunPos;
    }

    public double getMEDiffGen() {
        return MEDiffGen;
    }

    public void setMEDiffGen(double MEDiffGen) {
        this.MEDiffGen = MEDiffGen;
    }

    public double getMEDirGen() {
        return MEDirGen;
    }

    public void setMEDirGen(double MEDirGen) {
        this.MEDirGen = MEDirGen;
    }

    public double getMEReflGen() {
        return MEReflGen;
    }

    public void setMEReflGen(double MEReflGen) {
        this.MEReflGen = MEReflGen;
    }

    public double getMEGlobalGenEffective() {
        return MEGlobalGenEffective;
    }

    public void setMEGlobalGenEffective(double MEGlobalGenEffective) {
        this.MEGlobalGenEffective = MEGlobalGenEffective;
    }

    public double getMEGlobalGen() {
        return MEGlobalGen;
    }

    public void setMEGlobalGen(double MEGlobalGen) {
        this.MEGlobalGen = MEGlobalGen;
    }

    public double getAlbedo() {
        return Albedo;
    }

    public void setAlbedo(double albedo) {
        Albedo = albedo;
    }

    public double getAe() {
        return Ae;
    }

    public void setAe(double ae) {
        Ae = ae;
    }

    public double getYe() {
        return Ye;
    }

    public void setYe(double ye) {
        Ye = ye;
    }

    public double getHimmelsklarheitsindex() {
        return Himmelsklarheitsindex;
    }

    public void setHimmelsklarheitsindex(double himmelsklarheitsindex) {
        Himmelsklarheitsindex = himmelsklarheitsindex;
    }

    public double getDelta() {
        return Delta;
    }

    public void setDelta(double delta) {
        Delta = delta;
    }

    public double getAirMass() {
        return AirMass;
    }

    public void setAirMass(double airMass) {
        AirMass = airMass;
    }

    public double getAoiProjection() {
        return AoiProjection;
    }

    public void setAoiProjection(double aoiProjection) {
        AoiProjection = aoiProjection;
    }

    public double getF1() {
        return F1;
    }

    public void setF1(double f1) {
        F1 = f1;
    }

    public double getF2() {
        return F2;
    }

    public void setF2(double f2) {
        F2 = f2;
    }

    public double getF11() {
        return F11;
    }

    public void setF11(double f11) {
        F11 = f11;
    }

    public double getF12() {
        return F12;
    }

    public void setF12(double f12) {
        F12 = f12;
    }

    public double getF13() {
        return F13;
    }

    public void setF13(double f13) {
        F13 = f13;
    }

    public double getF21() {
        return F21;
    }

    public void setF21(double f21) {
        F21 = f21;
    }

    public double getF22() {
        return F22;
    }

    public void setF22(double f22) {
        F22 = f22;
    }

    public double getF23() {
        return F23;
    }

    public void setF23(double f23) {
        F23 = f23;
    }

    public double getA() {
        return A;
    }

    public void setA(double a) {
        A = a;
    }

    public double getB() {
        return B;
    }

    public void setB(double b) {
        B = b;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getAoi() {
        return Aoi;
    }

    public void setAoi(double aoi) {
        Aoi = aoi;
    }

    public double getProjectionRatio() {
        return ProjectionRatio;
    }

    public void setProjectionRatio(double projectionRatio) {
        ProjectionRatio = projectionRatio;
    }

    public double getEDirHorExtra() {
        return EDirHorExtra;
    }

    public void setEDirHorExtra(double EDirHorExtra) {
        this.EDirHorExtra = EDirHorExtra;
    }

    public double getAnisotropyIndex() {
        return AnisotropyIndex;
    }

    public void setAnisotropyIndex(double anisotropyIndex) {
        AnisotropyIndex = anisotropyIndex;
    }
}
