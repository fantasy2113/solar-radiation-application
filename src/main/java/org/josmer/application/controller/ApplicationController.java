package org.josmer.application.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
public class ApplicationController {

    @RequestMapping("/app")
    public String app() {
        try {
            return new String(Files.readAllBytes(Paths.get("web/app.html")));
        } catch (IOException e) {
            return e.toString();
        }
    }

}
