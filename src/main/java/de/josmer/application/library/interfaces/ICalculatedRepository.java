package de.josmer.application.library.interfaces;

import de.josmer.application.entities.Calculated;

import java.util.List;

public interface ICalculatedRepository {
    List<Calculated> calculateds(double[] eGlobHorMonthly, double lon, double lat, int ae, int ye, int year);
}
