package de.josmer.springboot.repositories;

import de.josmer.springboot.base.irradiation.ComputedIrradiation;
import de.josmer.springboot.base.irradiation.SolarIrradiation;
import de.josmer.springboot.entities.SolIrr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Component
public final class SolIrrRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(SolIrrRepository.class.getName());

    public List<SolIrr> getIrradiation(double[] eGlobHorMonthly, double lon, double lat, int ae, int ye, int year) {
        List<SolIrr> irradiation = new LinkedList<>();
        LocalDateTime dt = LocalDateTime.of(year, 1, 1, 0, 30, 0, 0);
        SolarIrradiation solarIrradiation = new SolarIrradiation(lat, lon, eGlobHorMonthly, dt, ye, ae);
        solarIrradiation.computeParallel();
        ComputedIrradiation computedIrradiation = solarIrradiation.getComputedIrradiation();
        addIrradiation(ae, ye, irradiation, dt, computedIrradiation);
        return irradiation;
    }

    private void addIrradiation(int ae, int ye, List<SolIrr> irradiation, LocalDateTime dt, ComputedIrradiation computedIrradiation) {
        for (int monthIndex = 0; monthIndex < 12; monthIndex++) {
            if (isAdd(computedIrradiation, monthIndex)) {
                SolIrr solIrr = new SolIrr();
                solIrr.seteGlobHor(computedIrradiation.getMonthHor(monthIndex) / 1000);
                solIrr.seteGlobGen(computedIrradiation.getMonthInc(monthIndex) / 1000);
                solIrr.setAe(ae);
                solIrr.setYe(ye);
                solIrr.setCalculatedDate(getDate(dt.getYear(), (monthIndex + 1)));
                irradiation.add(solIrr);
            }
        }
    }

    private boolean isAdd(ComputedIrradiation computedIrradiation, int monthIndex) {
        return computedIrradiation.getMonthHor(monthIndex) > 0 && computedIrradiation.getMonthInc(monthIndex) > 0;
    }

    private String getDate(int year, int month) {
        String parsedMonth = (month < 10) ? "0" + month : String.valueOf(month);
        return year + "-" + parsedMonth;
    }
}
