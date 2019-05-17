package de.orbis.application.controller;

import de.orbis.application.model.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class LogController {
    private final LogRepository logRepository;

    @Autowired
    public LogController(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @GetMapping(value = "logs", produces = MediaType.TEXT_HTML_VALUE)
    public final String getLogHtml() {
        return generateLogHtml(logRepository.getLogs());
    }

    private String generateLogHtml(final List<String> logs) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><title>Logs</title><body>");
        html.append("<table style=\"text-align:left;\">");
        html.append("<tr>");
        html.append("<th style=\"border:1px solid black\">Logs:</th>");
        html.append("</tr>");
        logs.stream().map(log -> "<tr><td style=\"border:1px solid black;text-align:left;\">" + log + "</td></tr>").forEach(html::append);
        html.append("</table></body></html>");
        return html.toString();
    }
}
