package de.jos.dwdcdc.library.interfaces;

public interface ISolRad {

  int getGkhMax();

  void setGkhMax(int gkhMax);

  int getGkhMin();

  void setGkhMin(int gkhMin);

  int getGkrMax();

  void setGkrMax(int gkrMax);

  int getGkrMin();

  void setGkrMin(int gkrMin);

  int getRadiationDate();

  void setRadiationDate(int radiationDate);

  String getRadiationType();

  void setRadiationType(String radiationType);

  float getRadiationValue();

  void setRadiationValue(float radiationValue);
}
