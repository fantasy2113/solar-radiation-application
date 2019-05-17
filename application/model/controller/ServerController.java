package de.orbis.application.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class ServerController {

    @RequestMapping("/")
    public String getServerTime() {
        return "Server Time: " + LocalDateTime.now();
    }

}
