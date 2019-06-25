package de.josmer.dwdcdc.app.controller;

import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class ServerController {

    @RequestMapping("/server_time")
    public String getServerTime() {
        return "ServerTime: " + LocalDateTime.now();
    }
}
