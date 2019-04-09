package org.josmer.application.repositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.josmer.application.interfaces.ILogRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public final class LogRepository implements ILogRepository {

    private static final Logger LOGGER = LogManager.getLogger(LogRepository.class.getName());

    @Override
    public List<String> infoLogs() {
        return readLog("./logs/app.log");
    }

    @Override
    public List<String> errorLogs() {
        return readLog("./logs/error.log");
    }

    private List<String> readLog(final String path) {
        List<String> logs = getLogs(path);
        Collections.reverse(logs);
        return logs;
    }

    private List<String> getLogs(String path) {
        try (Stream<String> lines = Files.lines(Paths.get(path), StandardCharsets.ISO_8859_1)) {
            return lines.collect(Collectors.toList());
        } catch (IOException ex) {
            LOGGER.info(ex.getMessage());
            return new ArrayList<>();
        }
    }

}
