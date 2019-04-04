package org.josmer.controller;

import org.josmer.security.Authenticator;
import org.josmer.security.Key;
import org.josmer.utils.Toolbox;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.io.IOException;

@RestController
public class ApplicationController {

    @RequestMapping(value = "/app", method = RequestMethod.GET)
    public String app(@CookieValue("key") final String key) {
        if (!Key.check(key)) {
            return Toolbox.readFile("src/main/resources/static/html/accessDenied.html");
        }
        
        try {
         File file = new File("src/main/resources/static/test.txt");
        } catch(IOException ioe) {;
        }
        
        return Toolbox.readFile("src/main/resources/static/html/app.html");
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        
        return Toolbox.readFile("src/main/resources/static/html/index.html");
    }

    @RequestMapping(value = "/key", method = RequestMethod.GET)
    public String key(@RequestHeader(value = "login") final String login, @RequestHeader(value = "password") final String password) {
        if (!isValid(login, password)) {
            return Key.undefined();
        }
        return Key.get();
    }

    protected boolean isValid(final String login, final String password) {
        return new Authenticator().authenticate(login, password);
    }

}
