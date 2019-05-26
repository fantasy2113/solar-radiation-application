package de.josmer.dwdcdc.utils.interfaces;

import de.josmer.dwdcdc.utils.entities.SolRad;
import de.josmer.dwdcdc.utils.enums.SolRadTypes;

import java.util.List;

public interface IBasicSolRad {
    boolean isAlreadyExist(int date, SolRadTypes solRadTypes);

    void save(List<SolRad> solRads);
}
