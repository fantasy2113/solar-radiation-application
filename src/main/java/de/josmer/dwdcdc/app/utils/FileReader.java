package de.josmer.dwdcdc.app.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;

import de.josmer.dwdcdc.library.interfaces.IDataReader;

@Component
public final class FileReader implements IDataReader {

	@Override
	public String getDataAsString(final String pathToData) {
		try {
			return new String(Files.readAllBytes(Paths.get(pathToData)));
		} catch (IOException e) {
			return e.toString();
		}
	}
}
