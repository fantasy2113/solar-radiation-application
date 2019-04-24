package de.josmer.app.repositories;

import de.josmer.app.entities.SolRadiInc;
import de.josmer.app.library.interfaces.ISolarRadiationInclinedRepository;
import de.josmer.app.library.solar.SolarRadiationInclined;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SolarRepositoryInclinedRepository implements ISolarRadiationInclinedRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolarRepositoryInclinedRepository.class.getName());

    @Override
    public List<SolRadiInc> getSolarRadiationsInclined(double[] eGlobHorMonthly, double lon, double lat, int ae, int ye, int year) {
        List<SolRadiInc> solarEnergies = new LinkedList<>();
        LocalDateTime dt = LocalDateTime.of(year, 1, 1, 0, 30, 0, 0);
        SolarRadiationInclined solarRadiationInclined = new SolarRadiationInclined(lat, lon, eGlobHorMonthly, dt, ye, ae);
        double[] eGlobGenMonthly = solarRadiationInclined.getEGlobGenMonthly();
        double[] eGlobHorMonthlySynth = solarRadiationInclined.getEGlobHorMonthlySynth();
        try {
            for (int i = 0; i < 12; i++) {
                if (eGlobHorMonthlySynth[i] > 0 && eGlobGenMonthly[i] > 0) {
                    SolRadiInc solarEnergy = new SolRadiInc();
                    solarEnergy.seteGlobHor(eGlobHorMonthlySynth[i] / 1000);
                    solarEnergy.seteGlobGen(eGlobGenMonthly[i] / 1000);
                    solarEnergy.setAe(ae);
                    solarEnergy.setYe(ye);
                    solarEnergy.setCalculatedDate(getDate(dt.getYear(), (i + 1)));
                    solarEnergies.add(solarEnergy);
                }
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return solarEnergies;
    }

    private String getDate(int year, int month) {
        String parsedMonth = (month < 10) ? "0" + month : String.valueOf(month);
        return year + "-" + parsedMonth;
    }
}
