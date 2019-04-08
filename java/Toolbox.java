package com.orbis.coreserver.base;

import com.orbis.coreserver.base.enums.Rights;
import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public final class Toolbox {

    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    private Toolbox() {
    }

    public static boolean isNull(Object object) {
        return object == null;
    }

    public static boolean isEmptyOrNull(String string) {
        return string == null || string.equals("");
    }

    public static boolean isNotNull(Object object) {
        return object != null;
    }

    public static String getRandomString(int count, boolean letters, boolean numbers) {
        return RandomStringUtils.random(count, letters, numbers);
    }

    public static String dateTimeNow() {
        return LocalDateTime.now(ZoneId.of("Europe/Berlin")).format(DateTimeFormatter.ofPattern(PATTERN));

    }

    public static String parseDateTime(final Timestamp timestamp) {
        return new SimpleDateFormat(PATTERN).format(timestamp);
    }

    public static List<String> rights() {
        List<String> rights = new ArrayList<>();
        for (Rights right : Rights.values()) {
            rights.add(right.toString());
        }
        return rights;
    }

    public static Rights right(String expectedRight) {
        for (Rights right : Rights.values()) {
            if (expectedRight.equals(right.toString())) {
                return right;
            }
        }
        return null;
    }
}
