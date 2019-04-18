package de.josmer.application.repositories;

import de.josmer.application.entities.Calculated;
import de.josmer.application.library.interfaces.ICalculatedRepository;
import de.josmer.application.library.sun.CalcRadiation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Component
public class CalculatedRepository implements ICalculatedRepository {

    @Override
    public List<Calculated> calculateds(double[] eGlobHorMonthly, double lon, double lat, int ae, int ye, int year) {
        List<Calculated> calculateds = new LinkedList<>();
        LocalDateTime dt = LocalDateTime.of(year, 1, 1, 0, 30, 0, 0);

        CalcRadiation calcRadiation = new CalcRadiation(lat, lon, eGlobHorMonthly, dt, ye, ae);
        double[] eGlobGenMonthly = calcRadiation.getEGlobGenMonthly();
        double[] eGlobHorMonthlySynth = calcRadiation.getEGlobHorMonthlySynth();

        for (int i = 0; i < 12; i++) {

        }

        return calculateds;
    }
}
