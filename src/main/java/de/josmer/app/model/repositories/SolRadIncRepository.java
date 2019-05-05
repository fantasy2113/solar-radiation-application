package de.josmer.app.model.repositories;

import de.josmer.app.library.interfaces.ISolRadIncRepository;
import de.josmer.app.library.solar.Irradiation;
import de.josmer.app.model.entities.SolRadInc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Component
public class SolRadIncRepository implements ISolRadIncRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolRadIncRepository.class.getName());

    @Override
    public List<SolRadInc> getSolRadInc(double[] eGlobHorMonthly, double lon, double lat, int ae, int ye, int year) {
        List<SolRadInc> solarEnergies = new LinkedList<>();
        LocalDateTime dt = LocalDateTime.of(year, 1, 1, 0, 30, 0, 0);
        Irradiation irradiation = new Irradiation(lat, lon, eGlobHorMonthly, dt, ye, ae);
        double[] eGlobGenMonthly = irradiation.getEGlobGenMonthly();
        double[] eGlobHorMonthlySynth = irradiation.getEGlobHorMonthlySynth();
        try {
            for (int i = 0; i < 12; i++) {
                if (eGlobHorMonthlySynth[i] > 0 && eGlobGenMonthly[i] > 0) {
                    SolRadInc solarEnergy = new SolRadInc();
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