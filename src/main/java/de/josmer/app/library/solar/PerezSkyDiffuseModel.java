package de.josmer.app.library.solar;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

class PerezSkyDiffuseModel {

    private static final String F_11 = "F11";
    private static final String F_12 = "F12";
    private static final String F_13 = "F13";
    private static final String F_21 = "F21";
    private static final String F_22 = "F22";
    private static final String F_23 = "F23";
    private static Map<String, double[]> fTabelle;
    private final double lon;
    private final double lat;
    private final SolarPostion sunPos;
    private final double albedo;
    private final double ae;
    private final double ye;
    private double eDiffGen;
    private double eDirGen;
    private double eReflGen;
    private double eGlobalGen;
    private double himmelsklarheitsindex;
    private double delta;
    private double airMass;
    private double aoiProjection;
    private double f1;
    private double f2;
    private double f11;
    private double f12;
    private double f13;
    private double f21;
    private double f22;
    private double f23;
    private double a;
    private double b;
    private double eDirHorExtra;

    PerezSkyDiffuseModel(double ye, double ae, double lat, double lon, double albedo) {
        this.ye = ye;
        this.ae = ae;
        this.lat = lat;
        this.lon = lon;
        this.albedo = albedo;
        this.sunPos = new SolarPostion();
    }

    public static void initFTabelle() {
        if (fTabelle == null) {
            fTabelle = new HashMap<>();
            fTabelle.put(F_11, new double[]{-0.008, 0.130, 0.330, 0.568, 0.873, 1.132, 1.060, 0.678});
            fTabelle.put(F_12, new double[]{0.588, 0.683, 0.487, 0.187, -0.392, -1.237, -1.600, -0.327});
            fTabelle.put(F_13, new double[]{-0.062, -0.151, -0.221, -0.295, -0.362, -0.412, -0.359, -0.250});
            fTabelle.put(F_21, new double[]{-0.060, -0.019, 0.055, 0.109, 0.226, 0.288, 0.264, 0.156});
            fTabelle.put(F_22, new double[]{0.072, 0.066, -0.064, -0.152, -0.462, -0.823, -1.127, -1.377});
            fTabelle.put(F_23, new double[]{-0.022, -0.029, -0.026, -0.014, 0.001, 0.056, 0.131, 0.251});
        }
    }

    private void setIndexFTabelle() {
        if (himmelsklarheitsindex >= 1.0 && himmelsklarheitsindex < 1.065) {
            setFValues(0);
        } else if (himmelsklarheitsindex >= 1.065 && himmelsklarheitsindex < 1.230) {
            setFValues(1);
        } else if (himmelsklarheitsindex >= 1.23 && himmelsklarheitsindex < 1.5) {
            setFValues(2);
        } else if (himmelsklarheitsindex >= 1.5 && himmelsklarheitsindex < 1.95) {
            setFValues(3);
        } else if (himmelsklarheitsindex >= 1.95 && himmelsklarheitsindex < 2.8) {
            setFValues(4);
        } else if (himmelsklarheitsindex >= 2.8 && himmelsklarheitsindex < 4.5) {
            setFValues(5);
        } else if (himmelsklarheitsindex >= 4.5 && himmelsklarheitsindex < 6.2) {
            setFValues(6);
        } else if (himmelsklarheitsindex >= 6.2) {
            setFValues(7);
        }
    }

    private void setFValues(int index) {
        f11 = fTabelle.get(F_11)[index];
        f12 = fTabelle.get(F_12)[index];
        f13 = fTabelle.get(F_13)[index];
        f21 = fTabelle.get(F_21)[index];
        f22 = fTabelle.get(F_22)[index];
        f23 = fTabelle.get(F_23)[index];
    }

    private void setF1AndF2() {
        double z = Utils.getRad(sunPos.getZenith());
        f1 = (f11 + f12 * delta + f13 * z);
        f1 = Math.max(f1, 0);
        f2 = (f21 + f22 * delta + f23 * z);
        f2 = Math.max(f2, 0);
    }

    private void setAoi() {
        aoiProjection = Utils.cos(ye) * Utils.cos(sunPos.getZenith()) + Utils.sin(ye) * Utils.sin(sunPos.getZenith()) * Utils.cos(sunPos.getAs() - ae);
    }

    private void setDelta(double eDiffHor) {
        delta = eDiffHor * airMass / eDirHorExtra;
    }

    private void setAirMass() {
        airMass = (1.0 / (Utils.cos(sunPos.getZenith()) + 0.50572 * (Math.pow((6.07995 + (90 - sunPos.getZenith())), -1.6364))));
    }

    private void setAandB() {
        a = aoiProjection;
        a = Math.max(a, 0);
        b = Utils.cos(sunPos.getZenith());
        b = Math.max(b, Utils.cos(85));
    }

    private void setEReflGen(double eGlobalHor) {
        eReflGen = eGlobalHor * albedo * (1.0 - Utils.cos(ye)) * 0.5;
    }

    private void setEDirGen(double eDirHor) {
        double cosSolarZenith = Utils.cos(sunPos.getZenith());
        double ratio = aoiProjection / cosSolarZenith;
        eDirGen = eDirHor * ratio;
        eDirGen = Math.max(eDirGen, 0);
    }

    private void setHimmelsklarheitsindex(double eDiffHor, double eDirHor) {
        double z = Utils.getRad(sunPos.getZenith());
        himmelsklarheitsindex = ((eDiffHor + eDirHor) / eDiffHor + Utils.K * Math.pow(z, 3)) / (1 + Utils.K * Math.pow(z, 3));
    }

    private void setMEDiffGenPerez(double eDiffHor) {
        double term1 = 0.5 * (1 - f1) * (1 + Utils.cos(ye));
        double term2 = f1 * a / b;
        double term3 = f2 * Utils.sin(ye);
        eDiffGen = Math.max(0, eDiffHor * (term1 + term2 + term3));
    }

    private void seteDirHorExtra(LocalDateTime dt) {
        double localB = sunPos.getSimpleDayAngle(dt.getDayOfYear(), dt.getYear());
        double roverR0Sqrd = (1.00011 + 0.034221 * Math.cos(localB) + 0.00128 * Math.sin(localB) + 0.000719 * Math.cos(2 * localB) + 7.7e-05 * Math.sin(2 * localB));
        eDirHorExtra = Utils.EO * roverR0Sqrd;
    }

    private double getEDiffHor(double eGlobalHor) {
        final double kt = eGlobalHor / (Utils.EO * Utils.sin(sunPos.getYs()));
        if (kt <= 3.0) {
            return eGlobalHor * (1.02 - 0.254 * kt + 0.0123 * Utils.sin(sunPos.getYs()));
        }

        if (kt > 3.0 && kt < 0.78) {
            return eGlobalHor * (1.4 - 1.749 * kt + 0.177 * Utils.sin(sunPos.getYs()));
        }

        return eGlobalHor * (0.486 * kt - 0.182 * Utils.sin(sunPos.getYs()));
    }

    void calculateHour(double eGlobalHor, LocalDateTime dt) {
        LocalDateTime dateTime = LocalDateTime.of(dt.getYear(), dt.getMonthValue(), dt.getDayOfMonth(), dt.getHour(), dt.getMinute(), 0, 0);
        sunPos.calculate(dateTime, lat, lon);
        if (eGlobalHor > 0 && sunPos.getYs() > 0) {
            setAoi();
            final double eDiffHor = getEDiffHor(eGlobalHor);
            final double eDirHor = eGlobalHor - eDiffHor;
            seteDirHorExtra(dt);
            setEDirGen(eDirHor);
            setEReflGen(eGlobalHor);
            setAirMass();
            setDelta(eDiffHor);
            setHimmelsklarheitsindex(eDiffHor, eDirHor);
            setIndexFTabelle();
            setF1AndF2();
            setAandB();
            setMEDiffGenPerez(eDiffHor);
            eGlobalGen = eDiffGen + eDirGen + eReflGen;
        }
    }

    double getEGlobalGen() {
        return eGlobalGen;
    }

}
