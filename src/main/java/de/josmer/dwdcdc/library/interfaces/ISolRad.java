package de.josmer.dwdcdc.library.interfaces;

public interface ISolRad {
    int getRadiationDate();

    void setRadiationDate(int radiationDate);

    int getGkhMin();

    void setGkhMin(int gkhMin);

    int getGkhMax();

    void setGkhMax(int gkhMax);

    int getGkrMin();

    void setGkrMin(int gkrMin);

    int getGkrMax();

    void setGkrMax(int gkrMax);

    float getRadiationValue();

    void setRadiationValue(float radiationValue);

    String getRadiationType();

    void setRadiationType(String radiationType);
}
