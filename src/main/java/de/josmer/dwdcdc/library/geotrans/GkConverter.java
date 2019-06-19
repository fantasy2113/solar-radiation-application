package de.josmer.dwdcdc.library.geotrans;

import de.josmer.dwdcdc.library.interfaces.IGaussKruger;

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

    private int getGkValues(final double gk) {
        Integer gkMin = (int) gk;
        while (isIncrement(gkMin)) {
            gkMin--;
        }
        return gkMin;
    }

    private boolean isIncrement(final Integer gkMin) {
        return !gkMin.toString().endsWith("500");
    }
}
