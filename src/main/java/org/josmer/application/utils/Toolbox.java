package org.josmer.application.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Toolbox {

    public static String readFile(final String file) {
        try {
            return new String(Files.readAllBytes(Paths.get(file)));
        } catch (IOException e) {
            return e.toString();
        }
    }
}
