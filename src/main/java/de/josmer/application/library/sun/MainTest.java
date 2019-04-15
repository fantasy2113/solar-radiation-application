package de.josmer.application.library.sun;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

class MainTest {
    public static void main(String[] args) {

        LocalDateTime dt = LocalDateTime.of(2010, 1, 1, 0, 30, 0, 0);
        double ye = 90; // tilt
        double ae = 0; // 180=south, 0=nord, 90=east, 270=west
        double lat = 52.5;
        double lon = 13.5;

        System.out.println("### Solar Radiation Test ###");
        System.out.println("<== Input");
        System.out.println("Azimuth Ae = " + ae);
        System.out.println("Tilt Ye = " + ye);
        System.out.println("Lat = " + lat);
        System.out.println("Lon = " + lon);

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
            int daysInMonth = Calc.getDaysInMonth(dt.getYear(), dt.getMonthValue());
            double eDiffHor = diffArr[month];
            double eDirHor = dirArr[month];
            double eGlobalHor = eDiffHor + eDirHor;
            double dayVal = eGlobalHor / Calc.getDaysInMonth(dt.getYear(), dt.getMonthValue());
            List<double[]> dayEList = new LinkedList<>();
            for (int day = 0; day < daysInMonth; day++) {
                TagModel tagModell = new TagModel();
                LocalDateTime dtTag = LocalDateTime.of(dt.getYear(), month + 1, day + 1, 0, 30);
                double[] eGlobalHorArr = tagModell.calculateDay(dtTag, dayVal, lat, lon);
                dayEList.add(eGlobalHorArr);
            }
            for (int day = 0; day < daysInMonth; day++) {
                double[] eGlobalHorArr = dayEList.get(day);
                for (int hour = 0; hour < 24; hour++) {
                    Radiation radiation = new Radiation(ye, ae, lat, lon, 0.2);
                    radiation.calculateHour(eGlobalHorArr[hour], dt);
                    yearTAGModelHorSum += eGlobalHorArr[hour];
                    yearSumGenGEN += radiation.geteGlobalGen();
                    dt = dt.plusHours(1);
                }
            }
        }

        System.out.println("Year EGlobalHor (TagModel) = " + yearTAGModelHorSum / 1000);
        System.out.println("Year EGlobalInc = " + yearSumGenGEN / 1000);
    }
}
