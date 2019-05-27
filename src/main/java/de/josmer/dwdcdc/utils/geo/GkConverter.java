package de.josmer.dwdcdc.utils.geo;

import de.josmer.dwdcdc.utils.interfaces.IGaussKruger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.OptionalInt;

public class GkConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(GkConverter.class.getName());
    private final IGaussKruger gaussKruger;

    public GkConverter(IGaussKruger gaussKruger) {
        this.gaussKruger = gaussKruger;
        try {
            this.gaussKruger.compute();
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }


    public OptionalInt getRechtswert() {
        try {
            if (String.valueOf(gaussKruger.getRechtswert()).startsWith("5")) {
                return OptionalInt.of(getGkValues(gaussKruger.getRechtswert() - 1600000));
            } else if (String.valueOf(gaussKruger.getRechtswert()).startsWith("4")) {
                return OptionalInt.of(getGkValues(gaussKruger.getRechtswert() - 800000));
            } else if (String.valueOf(gaussKruger.getRechtswert()).startsWith("3")) {
                return OptionalInt.of(getGkValues(gaussKruger.getRechtswert()));
            } else if (String.valueOf(gaussKruger.getRechtswert()).startsWith("2")) {
                return OptionalInt.of(getGkValues(gaussKruger.getRechtswert() + 800000));
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return OptionalInt.empty();
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
