package de.josmer.app.library.interfaces;

import de.josmer.app.model.entities.SolRadInc;

import java.util.List;

public interface ISolRadIncRepository {

    List<SolRadInc> getSolRadInc(double[] eGlobHorMonthly, double lon, double lat, int ae, int ye, int year);
}
