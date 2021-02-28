package de.jos.dwdcdc.app.repositories;

import de.jos.dwdcdc.app.entities.SolIrr;
import de.jos.dwdcdc.app.interfaces.ISolIrrRepository;
import de.jos.dwdcdc.library.irradiation.ComputedYear;
import de.jos.dwdcdc.library.irradiation.SolarIrradiation;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public final class SolIrrRepository implements ISolIrrRepository {

  private void addIrradiation(int ae, int ye, List<SolIrr> irradiation, int year, ComputedYear computedYear) {
    for (int monthIndex = 0; monthIndex < 12; monthIndex++) {
      if (isAdd(computedYear, monthIndex)) {
        SolIrr solIrr = new SolIrr();
        solIrr.seteGlobHor(toKiloWatt(computedYear.getMonthHor(monthIndex)));
        solIrr.seteGlobGen(toKiloWatt(computedYear.getMonthInc(monthIndex)));
        solIrr.setAe(ae);
        solIrr.setYe(ye);
        solIrr.setCalculatedDate(getDate(year, toMonth(monthIndex)));
        irradiation.add(solIrr);
      }
    }
  }

  private String getDate(int year, int month) {
    String parsedMonth = (month < 10) ? "0" + month : String.valueOf(month);
    return year + "-" + parsedMonth;
  }

  @Override
  public LinkedList<SolIrr> getIrradiation(double[] eGlobHorMonthly, double lon, double lat, int ae, int ye, int year) {
    LinkedList<SolIrr> irradiation = new LinkedList<>();
    SolarIrradiation solarIrradiation = new SolarIrradiation(lat, lon, eGlobHorMonthly, year, ye, ae);
    solarIrradiation.compute();
    ComputedYear computedYear = solarIrradiation.getComputedYear();
    addIrradiation(ae, ye, irradiation, year, computedYear);
    return irradiation;
  }

  private boolean isAdd(ComputedYear computedYear, int monthIndex) {
    return computedYear.getMonthHor(monthIndex) > 0 && computedYear.getMonthInc(monthIndex) > 0;
  }

  private double toKiloWatt(double monthHor) {
    return monthHor / 1000;
  }

  private int toMonth(int monthIndex) {
    return monthIndex + 1;
  }
}
