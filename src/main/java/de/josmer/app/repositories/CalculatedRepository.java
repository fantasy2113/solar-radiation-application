package de.josmer.app.repositories;

import de.josmer.app.entities.Calculated;
import de.josmer.app.library.interfaces.ICalculatedRepository;
import de.josmer.app.library.sun.CalcRadiation;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CalculatedRepository implements ICalculatedRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalculatedRepository.class.getName());

    @Override
    public List<Calculated> calculateds(double[] eGlobHorMonthly, double lon, double lat, int ae, int ye, int year) {
        List<Calculated> calculateds = new LinkedList<>();
        LocalDateTime dt = LocalDateTime.of(year, 1, 1, 0, 30, 0, 0);
        CalcRadiation calcRadiation = new CalcRadiation(lat, lon, eGlobHorMonthly, dt, ye, ae);
        double[] eGlobGenMonthly = calcRadiation.getEGlobGenMonthly();
        double[] eGlobHorMonthlySynth = calcRadiation.getEGlobHorMonthlySynth();
        try {
            for (int i = 0; i < 12; i++) {
                if (eGlobHorMonthlySynth[i] > 0 && eGlobGenMonthly[i] > 0) {
                    Calculated calculated = new Calculated();
                    calculated.seteGlobHor(eGlobHorMonthlySynth[i] / 1000);
                    calculated.seteGlobGen(eGlobGenMonthly[i] / 1000);
                    calculated.setAe(ae);
                    calculated.setYe(ye);
                    calculated.setCalculatedDate(getDate(dt.getYear(), (i + 1)));
                    calculateds.add(calculated);
                }
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return calculateds;
    }

    private String getDate(int year, int month) {
        String parsedMonth = (month < 10) ? "0" + month : String.valueOf(month);
        return year + "-" + parsedMonth;
    }
}
