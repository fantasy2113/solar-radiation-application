package org.josmer.controller;

import org.josmer.security.Authenticator;
import org.josmer.security.Key;
import org.josmer.utils.Toolbox;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {

    @RequestMapping("/app")
    public String app(@CookieValue("key") final String key) {
        if (!Key.check(key)) {
            return Toolbox.readFile("src/main/resources/static/html/accessDenied.html");
        }
        return Toolbox.readFile("src/main/resources/static/html/app.html");
    }

    @RequestMapping("/")
    public String index() {
        return Toolbox.readFile("src/main/resources/static/html/index.html");
    }

    @RequestMapping("/key")
    public String key(@RequestHeader(value = "login") final String login, @RequestHeader(value = "password") final String password) {
        if (!isValid(login, password)) {
            return Key.undefined();
        }
        return Key.get();
    }

    private boolean isValid(final String login, final String password) {
        return new Authenticator().authenticate(login, password);
    }

}
