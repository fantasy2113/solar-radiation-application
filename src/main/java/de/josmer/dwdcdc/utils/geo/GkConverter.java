package de.josmer.dwdcdc.utils.geo;

import de.josmer.dwdcdc.utils.interfaces.IGaussKruger;

import java.util.OptionalInt;

public class GkConverter {
    private final IGaussKruger gaussKruger;

    public GkConverter(IGaussKruger gaussKruger) {
        this.gaussKruger = gaussKruger;
        this.gaussKruger.compute();
    }


    public OptionalInt getRechtswert() {
        switch (gaussKruger.getMeridianstreifen()) {
            case 5:
                return OptionalInt.of(getGkValues(gaussKruger.getRechtswert() - 1600000));
            case 4:
                return OptionalInt.of(getGkValues(gaussKruger.getRechtswert() - 800000));
            case 3:
                return OptionalInt.of(getGkValues(gaussKruger.getRechtswert()));
            case 2:
                return OptionalInt.of(getGkValues(gaussKruger.getRechtswert() + 800000));
            default:
                return OptionalInt.empty();
        }
    }

    public int getHochwert() {
        return getGkValues(gaussKruger.getHochwert());
    }

    private int getGkValues(final double value) {
        Integer gkMin = (int) value;
        while (!gkMin.toString().endsWith("500")) {
            gkMin--;
        }
        return gkMin;
    }
}
