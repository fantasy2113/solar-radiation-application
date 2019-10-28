package de.josmer.dwdcdc.app.repositories;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import de.josmer.dwdcdc.app.interfaces.ILogRepository;

@Component
public final class LogRepository implements ILogRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogRepository.class.getName());

	@Override
	public List<String> getLogs() {
		return readLog("logs/spring-boot-logger.log");
	}

	private List<String> getLogs(String path) {
		try (Stream<String> lines = Files.lines(Paths.get(path), StandardCharsets.ISO_8859_1)) {
			return lines.collect(Collectors.toList());
		} catch (IOException e) {
			LOGGER.info(e.toString());
			return new ArrayList<>();
		}
	}

	private List<String> readLog(final String path) {
		List<String> logs = getLogs(path);
		Collections.reverse(logs);
		return logs;
	}

}
