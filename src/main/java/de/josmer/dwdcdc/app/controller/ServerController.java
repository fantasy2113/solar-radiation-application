package de.josmer.dwdcdc.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public final class ServerController {

    @RequestMapping("/server_time")
    public String getServerTime() {
        return "ServerTime: " + LocalDateTime.now();
    }
}
