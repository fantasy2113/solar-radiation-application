package de.josmer.dwdcdc.springboot.base.irradiation;

import java.time.LocalDateTime;
import java.util.Map;

final class PerezSkyDiffModel {
    private static final String F_11 = "F11";
    private static final String F_12 = "F12";
    private static final String F_13 = "F13";
    private static final String F_21 = "F21";
    private static final String F_22 = "F22";
    private static final String F_23 = "F23";
    private static final Map<String, double[]> fTab = getFTab();
    private final double lon;
    private final double lat;
    private final SolarPosition solarPosition;
    private final double albedo;
    private final double ae;
    private final double ye;
    private double eDiffInc;
    private double eDirInc;
    private double eRefInc;
    private double skyClearnessIndex;
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

    PerezSkyDiffModel(double ye, double ae, double lat, double lon, double albedo) {
        this.ye = ye;
        this.ae = ae;
        this.lat = lat;
        this.lon = lon;
        this.albedo = albedo;
        this.solarPosition = new SolarPosition();
    }

    private static Map<String, double[]> getFTab() {
        return Map.of(
                F_11, new double[]{-0.008, 0.130, 0.330, 0.568, 0.873, 1.132, 1.060, 0.678},
                F_12, new double[]{0.588, 0.683, 0.487, 0.187, -0.392, -1.237, -1.600, -0.327},
                F_13, new double[]{-0.062, -0.151, -0.221, -0.295, -0.362, -0.412, -0.359, -0.250},
                F_21, new double[]{-0.060, -0.019, 0.055, 0.109, 0.226, 0.288, 0.264, 0.156},
                F_22, new double[]{0.072, 0.066, -0.064, -0.152, -0.462, -0.823, -1.127, -1.377},
                F_23, new double[]{-0.022, -0.029, -0.026, -0.014, 0.001, 0.056, 0.131, 0.251});
    }

    void compute(double eGlobalHor, SolarDateTime dt) {
        resetValues();
        solarPosition.compute(dt, lat, lon);

        if (eGlobalHor <= 0 || solarPosition.getYs() <= 0) {
            return;
        }

        computeAoi();
        final double eDiffHor = getEDiffHor(eGlobalHor);
        final double eDirHor = getDirHor(eGlobalHor, eDiffHor);
        computeEDirHorExtra(dt);
        computeEDirInc(eDirHor);
        computeERefInc(eGlobalHor);
        computeAirMass();
        computeDelta(eDiffHor);
        computeSkyClearnessIndex(eDiffHor, eDirHor);
        computeIndexFTab();
        computeF1F2();
        computeAB();
        computeMEDiffIncPerez(eDiffHor);
    }

    double getHourlyEnergy() {
        return eDiffInc + eDirInc + eRefInc;
    }

    private double getDirHor(double eGlobalHor, double eDiffHor) {
        return eGlobalHor - eDiffHor;
    }

    private void computeIndexFTab() {
        if (skyClearnessIndex >= 1.0 && skyClearnessIndex < 1.065) {
            setFValues(0);
        } else if (skyClearnessIndex >= 1.065 && skyClearnessIndex < 1.230) {
            setFValues(1);
        } else if (skyClearnessIndex >= 1.23 && skyClearnessIndex < 1.5) {
            setFValues(2);
        } else if (skyClearnessIndex >= 1.5 && skyClearnessIndex < 1.95) {
            setFValues(3);
        } else if (skyClearnessIndex >= 1.95 && skyClearnessIndex < 2.8) {
            setFValues(4);
        } else if (skyClearnessIndex >= 2.8 && skyClearnessIndex < 4.5) {
            setFValues(5);
        } else if (skyClearnessIndex >= 4.5 && skyClearnessIndex < 6.2) {
            setFValues(6);
        } else if (skyClearnessIndex >= 6.2) {
            setFValues(7);
        }
    }

    private void setFValues(int index) {
        f11 = fTab.get(F_11)[index];
        f12 = fTab.get(F_12)[index];
        f13 = fTab.get(F_13)[index];
        f21 = fTab.get(F_21)[index];
        f22 = fTab.get(F_22)[index];
        f23 = fTab.get(F_23)[index];
    }

    private void computeF1F2() {
        double z = Utils.getRad(solarPosition.getZenith());
        f1 = (f11 + f12 * delta + f13 * z);
        f1 = Math.max(f1, 0);
        f2 = (f21 + f22 * delta + f23 * z);
        f2 = Math.max(f2, 0);
    }

    private void computeAoi() {
        aoiProjection = Utils.cos(ye) * Utils.cos(solarPosition.getZenith()) + Utils.sin(ye) * Utils.sin(solarPosition.getZenith()) * Utils.cos(solarPosition.getAs() - ae);
    }

    private void computeDelta(double eDiffHor) {
        delta = eDiffHor * airMass / eDirHorExtra;
    }

    private void computeAirMass() {
        airMass = (1.0 / (Utils.cos(solarPosition.getZenith()) + 0.50572 * (Math.pow((6.07995 + (90 - solarPosition.getZenith())), -1.6364))));
    }

    private void computeAB() {
        a = aoiProjection;
        a = Math.max(a, 0);
        b = Utils.cos(solarPosition.getZenith());
        b = Math.max(b, Utils.cos(85));
    }

    private void computeERefInc(double eGlobalHor) {
        eRefInc = eGlobalHor * albedo * (1.0 - Utils.cos(ye)) * 0.5;
    }

    private void computeEDirInc(double eDirHor) {
        double cosSolarZenith = Utils.cos(solarPosition.getZenith());
        double ratio = aoiProjection / cosSolarZenith;
        eDirInc = eDirHor * ratio;
        eDirInc = Math.max(eDirInc, 0);
    }

    private void computeSkyClearnessIndex(double eDiffHor, double eDirHor) {
        double z = Utils.getRad(solarPosition.getZenith());
        skyClearnessIndex = ((eDiffHor + eDirHor) / eDiffHor + Utils.K * Math.pow(z, 3)) / (1 + Utils.K * Math.pow(z, 3));
    }

    private void computeMEDiffIncPerez(double eDiffHor) {
        double term1 = 0.5 * (1 - f1) * (1 + Utils.cos(ye));
        double term2 = f1 * a / b;
        double term3 = f2 * Utils.sin(ye);
        eDiffInc = Math.max(0, eDiffHor * (term1 + term2 + term3));
    }

    private void computeEDirHorExtra(SolarDateTime dt) {
        double be = solarPosition.getSimpleDayAngle(dt.getDayOfYear(), dt.getYear());
        double roverR0Sqrd = (1.00011 + 0.034221 * Math.cos(be) + 0.00128 * Math.sin(be) + 0.000719 * Math.cos(2 * be) + 7.7e-05 * Math.sin(2 * be));
        eDirHorExtra = Utils.EO * roverR0Sqrd;
    }

    private double getEDiffHor(double eGlobalHor) {
        final double kt = eGlobalHor / (Utils.EO * Utils.sin(solarPosition.getYs()));
        if (kt <= 3.0) {
            return eGlobalHor * (1.02 - 0.254 * kt + 0.0123 * Utils.sin(solarPosition.getYs()));
        } else if (kt > 3.0 && kt < 0.78) {
            return eGlobalHor * (1.4 - 1.749 * kt + 0.177 * Utils.sin(solarPosition.getYs()));
        } else {
            return eGlobalHor * (0.486 * kt - 0.182 * Utils.sin(solarPosition.getYs()));
        }
    }

    private LocalDateTime getDt(LocalDateTime dt) {
        return LocalDateTime.of(dt.getYear(), dt.getMonthValue(), dt.getDayOfMonth(), dt.getHour(), dt.getMinute(), 0, 0);
    }

    private void resetValues() {
        eDiffInc = 0;
        eDirInc = 0;
        eRefInc = 0;
    }
}
