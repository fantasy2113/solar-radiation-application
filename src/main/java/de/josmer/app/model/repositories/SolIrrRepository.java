package de.josmer.app.model.repositories;

import de.josmer.app.library.interfaces.ISolIrrRepository;
import de.josmer.app.library.solar.SolarIrradiation;
import de.josmer.app.model.entities.SolIrr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class SolIrrRepository implements ISolIrrRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolIrrRepository.class.getName());

    @Override
    public List<SolIrr> getSolRadInc(double[] eGlobHorMonthly, double lon, double lat, int ae, int ye, int year) {
        List<SolIrr> solarEnergies = new LinkedList<>();
        LocalDateTime dt = LocalDateTime.of(year, 1, 1, 0, 30, 0, 0);
        SolarIrradiation solarIrradiation = new SolarIrradiation(lat, lon, eGlobHorMonthly, dt, ye, ae);
        solarIrradiation.computeParallel();
        double[] computedEIncMonths = solarIrradiation.getComputedEIncMonths();
        double[] computedEHorMonths = solarIrradiation.getComputedEHorMonths();
        IntStream.range(0, 12).forEach(i -> {
            if (computedEHorMonths[i] > 0 && computedEIncMonths[i] > 0) {
                SolIrr solarEnergy = new SolIrr();
                solarEnergy.seteGlobHor(computedEHorMonths[i] / 1000);
                solarEnergy.seteGlobGen(computedEIncMonths[i] / 1000);
                solarEnergy.setAe(ae);
                solarEnergy.setYe(ye);
                solarEnergy.setCalculatedDate(getDate(dt.getYear(), (i + 1)));
                solarEnergies.add(solarEnergy);
            }
        });
        return solarEnergies;
    }

    private String getDate(int year, int month) {
        String parsedMonth = (month < 10) ? "0" + month : String.valueOf(month);
        return year + "-" + parsedMonth;
    }
}
