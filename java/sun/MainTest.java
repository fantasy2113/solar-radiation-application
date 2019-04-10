package de.josmer.application.sun;

public class MainTest {
    public static void Main(string[] args) {
        Dictionary <string, double> PRDict = new Dictionary<string, double>();
        PRDict.Add("Optimal, no shading (PR=0.8)", 0.8);
        PRDict.Add("Good, no shading (PR=0.75)", 0.75);
        PRDict.Add("Good, slightly shading (PR=0.70)", 0.7);
        PRDict.Add("Moderate, shading (PR=0.60)", 0.6);
        PRDict.Add("Bad, shading (PR=0.50)", 0.5);
        DateTime Dt = new DateTime(2010, 1, 1, 0, 30, 0, 0);
        double Ye = 32; // tilt
        double Ae = 160; // 180=south, 0=nord, 90=east, 270=west
        double lat = 52.5;
        double lon = 13.5;
        double albedo = 0.2;
        int diffMdel = 1;
        double shading = 3;
        Console.WriteLine("### Solar Irradiation Test ###");
        Console.WriteLine("<== Input");
        Console.WriteLine("Azimuth Ae = " + Ae);
        Console.WriteLine("Tilt Ye = " + Ye);
        Console.WriteLine("Lat = " + lat);
        Console.WriteLine("Lon = " + lon);
        Console.WriteLine("Albedo = " + albedo);
        Console.WriteLine("Shading Loss % = " + shading);
        Console.WriteLine("Diff-Model = " + diffMdel + " # Perez=1, Hay and Davies=2");
        double yearTAGModelHorSum = 0;
        double yearSumGenEffective = 0;
        double yearSumGenGEN = 0;
        double[] dirArr = new double[] { 5,12,31,69,81,81,71,67,41,19,10,4 }; // Berlin
        double[] diffArr = new double[] { 14,22,44,59,78,85,87,67,53,32,17,11 }; // Berlin
        for (int i = 0; i < 12; i++) {
            dirArr[i] = dirArr[i] * 1000;
            diffArr[i] = diffArr[i] * 1000;
            Console.WriteLine("Month: [" + (i + 1) + "] ==> [EDiffHor: " + diffArr[i] + "] & [EDirHor: " + dirArr[i] +"]");
        }
        Console.WriteLine("==> Output");
        for (int month = 0; month < 12; month++) {
            int daysInMonth = SunToolBox.GetDaysInMonth(Dt.Year, Dt.Month);
            //Console.WriteLine("\n####### ####### ####### ####### #######");
            //Console.WriteLine("\nDate = " + Dt.ToString("yyyy-MM"));
            double eDiffHor = diffArr[month];
            double eDirHor = dirArr[month]; ;
            double eGlobalHor = eDiffHor + eDirHor;
            double diffAmount = eDiffHor / eGlobalHor;
            double dirAmount = eDirHor / eGlobalHor;
            double dayVal = eGlobalHor / SunToolBox.GetDaysInMonth(Dt.Year, Dt.Month);
            List<double[]> dayEList = new List<double[]>();
            for (int day = 0; day < daysInMonth; day++) {
                TAGModel tagModell = new TAGModel();
                double[] eGlobalHorArr = tagModell.CalculateDay(Dt, dayVal, lat, lon);
                dayEList.Add(eGlobalHorArr);
            }
            for (int day = 0; day < daysInMonth; day++) {
                double[] eGlobalHorArr = dayEList[day];
                for (int hour = 0; hour < 24; hour++) {
                    Irradiation globalGen = new Irradiation(Ye, Ae, lat, lon, albedo);
                    globalGen.CalculateHour(eGlobalHorArr[hour] * diffAmount, eGlobalHorArr[hour] * dirAmount, eGlobalHorArr[hour], Dt, diffMdel, shading);
                    if (globalGen.MSunPos.MYsAtmosphericRefractionCorrection() > 0) {
                        yearTAGModelHorSum += eGlobalHorArr[hour];
                        yearSumGenEffective += globalGen.MEGlobalGenEffective;
                        yearSumGenGEN += globalGen.MEGlobalGen;
                    }
                    Dt = Dt.AddHours(1);
                }
            }
        }
        yearTAGModelHorSum = Math.Round(yearTAGModelHorSum / 1000, 1);
        yearSumGenGEN = Math.Round(yearSumGenGEN / 1000, 1);
        yearSumGenEffective = Math.Round(yearSumGenEffective / 1000, 1);
        Console.WriteLine("Year EGlobalHor (TAGModel) = " + yearTAGModelHorSum);
        Console.WriteLine("Year EGlobalInc = " + yearSumGenGEN);
        Console.WriteLine("Year EGlobalIncEff = " + yearSumGenEffective);
        Console.WriteLine("Press key to exit");
        Console.ReadLine();
}
