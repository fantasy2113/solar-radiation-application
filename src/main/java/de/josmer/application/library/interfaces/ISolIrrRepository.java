package de.josmer.application.library.interfaces;

import de.josmer.application.model.entities.SolIrr;

import java.util.List;

public interface ISolIrrRepository {

    List<SolIrr> getSolRadInc(double[] eGlobHorMonthly, double lon, double lat, int ae, int ye, int year);
}
