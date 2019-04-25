package de.josmer.app.model.repositories;

import de.josmer.app.library.interfaces.ISolRadiIncRepository;
import de.josmer.app.library.solar.GlobToInc;
import de.josmer.app.model.entities.SolRadiInc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Component
public class SolRadiIncRepository implements ISolRadiIncRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolRadiIncRepository.class.getName());

    @Override
    public List<SolRadiInc> getSolarRadiationsInclined(double[] eGlobHorMonthly, double lon, double lat, int ae, int ye, int year) {
        List<SolRadiInc> solarEnergies = new LinkedList<>();
        LocalDateTime dt = LocalDateTime.of(year, 1, 1, 0, 30, 0, 0);
        GlobToInc globToInc = new GlobToInc(lat, lon, eGlobHorMonthly, dt, ye, ae);
        double[] eGlobGenMonthly = globToInc.getEGlobGenMonthly();
        double[] eGlobHorMonthlySynth = globToInc.getEGlobHorMonthlySynth();
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
