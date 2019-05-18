package de.josmer.dwdcdc.springboot.base.utils;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public final class FileReader implements de.josmer.dwdcdc.springboot.base.interfaces.IFileReader {

    @Override
    public String asString(final String file) {
        try {
            return new String(Files.readAllBytes(Paths.get(file)));
        } catch (IOException e) {
            return e.toString();
        }
    }
}
