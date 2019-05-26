package de.josmer.dwdcdc.app.repositories;

import de.josmer.dwdcdc.app.entities.SolIrr;
import de.josmer.dwdcdc.app.interfaces.ISolIrrRepository;
import de.josmer.dwdcdc.utils.solar.ComputedIrradiation;
import de.josmer.dwdcdc.utils.solar.SolarIrradiation;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public final class SolIrrRepository implements ISolIrrRepository {

    @Override
    public LinkedList<SolIrr> getIrradiation(double[] eGlobHorMonthly, double lon, double lat, int ae, int ye, int year) {
        LinkedList<SolIrr> irradiation = new LinkedList<>();
        SolarIrradiation solarIrradiation = new SolarIrradiation(lat, lon, eGlobHorMonthly, year, ye, ae);
        solarIrradiation.computeParallel();
        ComputedIrradiation computedIrradiation = solarIrradiation.getComputedIrradiation();
        addIrradiation(ae, ye, irradiation, year, computedIrradiation);
        return irradiation;
    }

    private void addIrradiation(int ae, int ye, List<SolIrr> irradiation, int year, ComputedIrradiation computedIrradiation) {
        for (int monthIndex = 0; monthIndex < 12; monthIndex++) {
            if (isAdd(computedIrradiation, monthIndex)) {
                SolIrr solIrr = new SolIrr();
                solIrr.seteGlobHor(computedIrradiation.getMonthHor(monthIndex) / 1000);
                solIrr.seteGlobGen(computedIrradiation.getMonthInc(monthIndex) / 1000);
                solIrr.setAe(ae);
                solIrr.setYe(ye);
                solIrr.setCalculatedDate(getDate(year, (monthIndex + 1)));
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
