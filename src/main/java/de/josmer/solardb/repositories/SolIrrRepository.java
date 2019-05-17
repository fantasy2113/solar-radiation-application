package de.josmer.solardb.repositories;

import de.josmer.solardb.entities.SolIrr;
import de.josmer.solardb.irradiation.SolarIrradiation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Component
public final class SolIrrRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(SolIrrRepository.class.getName());

    public List<SolIrr> getSolRadInc(double[] eGlobHorMonthly, double lon, double lat, int ae, int ye, int year) {
        List<SolIrr> solarEnergies = new LinkedList<>();
        LocalDateTime dt = LocalDateTime.of(year, 1, 1, 0, 30, 0, 0);
        SolarIrradiation solarIrradiation = new SolarIrradiation(lat, lon, eGlobHorMonthly, dt, ye, ae);
        solarIrradiation.computeParallel();
        double[] eIncMonths = solarIrradiation.getEIncMonths();
        double[] eHorMonths = solarIrradiation.getEHorMonths();
        for (int i = 0; i < 12; i++) {
            if (eHorMonths[i] > 0 && eIncMonths[i] > 0) {
                SolIrr solIrr = new SolIrr();
                solIrr.seteGlobHor(eHorMonths[i] / 1000);
                solIrr.seteGlobGen(eIncMonths[i] / 1000);
                solIrr.setAe(ae);
                solIrr.setYe(ye);
                solIrr.setCalculatedDate(getDate(dt.getYear(), (i + 1)));
                solarEnergies.add(solIrr);
            }
        }
        return solarEnergies;
    }

    private String getDate(int year, int month) {
        String parsedMonth = (month < 10) ? "0" + month : String.valueOf(month);
        return year + "-" + parsedMonth;
    }
}
