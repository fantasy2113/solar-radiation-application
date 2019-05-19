package de.josmer.app.utils;

import de.josmer.libs.interfaces.IFileReader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public final class FileReader implements IFileReader {

    @Override
    public String asString(final String file) {
        try {
            return new String(Files.readAllBytes(Paths.get(file)));
        } catch (IOException e) {
            return e.toString();
        }
    }
}