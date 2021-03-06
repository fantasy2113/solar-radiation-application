package de.jos.dwdcdc.shared;

import de.jos.dwdcdc.library.enums.SolRadTypes;

import java.util.List;

public interface IBasicSolRad {

  boolean isAlreadyExist(int date, SolRadTypes solRadTypes);

  void save(List<ISolRad> solRads);
}
