package de.josmer.solardb.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public final class ServerController {

    @RequestMapping("/time")
    public String getServerTime() {
        return "Server Time: " + LocalDateTime.now();
    }
}
