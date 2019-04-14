package de.josmer.app.lib.sun;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class MainTest {
    public static void main(String[] args) {

        LocalDateTime Dt = LocalDateTime.of(2010, 1, 1, 0, 30, 0, 0);
        double Ye = 30; // tilt
        double Ae = 180; // 180=south, 0=nord, 90=east, 270=west
        double lat = 52.5;
        double lon = 13.5;
        double albedo = 0.2;
        int diffMdel = 1;
        System.out.println("### Solar Irradiation Test ###");
        System.out.println("<== Input");
        System.out.println("Azimuth Ae = " + Ae);
        System.out.println("Tilt Ye = " + Ye);
        System.out.println("Lat = " + lat);
        System.out.println("Lon = " + lon);
        System.out.println("Albedo = " + albedo);
        System.out.println("Diff-Model = " + diffMdel + " # Perez=1, Hay and Davies=2");
        double yearTAGModelHorSum = 0;
        double yearSumGenGEN = 0;
        double[] dirArr = new double[]{5, 12, 31, 69, 81, 81, 71, 67, 41, 19, 10, 4}; // Berlin
        double[] diffArr = new double[]{14, 22, 44, 59, 78, 85, 87, 67, 53, 32, 17, 11}; // Berlin
        double gesamt = 0;
        for (int i = 0; i < 12; i++) {
            dirArr[i] = dirArr[i] * 1000;
            diffArr[i] = diffArr[i] * 1000;
            gesamt += dirArr[i] + diffArr[i];
            System.out.println("Month: [" + (i + 1) + "] ==> [EDiffHor: " + diffArr[i] + "] & [EDirHor: " + dirArr[i] + "]");
        }
        System.out.println(gesamt / 1000);
        System.out.println("==> Output");
        for (int month = 0; month < 12; month++) {
            int daysInMonth = SunToolBox.GetDaysInMonth(Dt.getYear(), Dt.getMonthValue());
            //System.out.println("\n####### ####### ####### ####### #######");
            //System.out.println("\nDate = " + Dt.toString());
            double eDiffHor = diffArr[month];
            double eDirHor = dirArr[month];
            double eGlobalHor = eDiffHor + eDirHor;
            double diffAmount = eDiffHor / eGlobalHor;
            double dirAmount = eDirHor / eGlobalHor;
            double dayVal = eGlobalHor / SunToolBox.GetDaysInMonth(Dt.getYear(), Dt.getMonthValue());
            List<double[]> dayEList = new LinkedList<>();
            for (int day = 0; day < daysInMonth; day++) {
                TAGModel tagModell = new TAGModel();
                LocalDateTime dtTag = LocalDateTime.of(Dt.getYear(), month + 1, day + 1, 0, 30);
                double[] eGlobalHorArr = tagModell.CalculateDay(dtTag, dayVal, lat, lon);
                dayEList.add(eGlobalHorArr);
            }
            for (int day = 0; day < daysInMonth; day++) {
                double[] eGlobalHorArr = dayEList.get(day);
                for (int hour = 0; hour < 24; hour++) {
                    Irradiation globalGen = new Irradiation(Ye, Ae, lat, lon, albedo);
                    globalGen.CalculateHour(eGlobalHorArr[hour] * diffAmount, eGlobalHorArr[hour] * dirAmount, eGlobalHorArr[hour], Dt, diffMdel);
                    if (globalGen.getMSunPos().MYsAtmosphericRefractionCorrection() > 0) {
                        yearTAGModelHorSum += eGlobalHorArr[hour];
                        yearSumGenGEN += globalGen.getMEGlobalGen();
                    }
                    Dt = Dt.plusHours(1);
                }
            }
        }

        System.out.println("Year EGlobalHor (TAGModel) = " + yearTAGModelHorSum / 1000);
        System.out.println("Year EGlobalInc = " + yearSumGenGEN / 1000);
    }
}
