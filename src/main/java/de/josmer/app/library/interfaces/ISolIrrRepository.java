package de.josmer.app.library.interfaces;

import de.josmer.app.model.entities.SolIrr;

import java.util.List;

public interface ISolIrrRepository {

    List<SolIrr> getSolRadInc(double[] eGlobHorMonthly, double lon, double lat, int ae, int ye, int year);
}
