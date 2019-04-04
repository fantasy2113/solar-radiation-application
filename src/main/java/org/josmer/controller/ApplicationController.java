package org.josmer.controller;

import org.josmer.entities.Person;
import org.josmer.security.Authenticator;
import org.josmer.security.Key;
import org.josmer.utils.Toolbox;
import org.jxls.template.SimpleExporter;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class ApplicationController {

    @RequestMapping(value = "/app", method = RequestMethod.GET)
    public String app(@CookieValue("key") final String key) {
        if (!Key.check(key)) {
            return Toolbox.readFile("src/main/resources/static/html/accessDenied.html");
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

    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void export(HttpServletResponse response) {
        List<Person> persons = new ArrayList<>();
        persons.add(new Person());
        persons.add(new Person());
        persons.add(new Person());
        persons.add(new Person());
        persons.add(new Person());
        persons.add(new Person());
        persons.add(new Person());
        persons.add(new Person());
        List<String> headers = Arrays.asList("First Name", "Last Name");
        try {
            response.addHeader("Content-disposition", "attachment; filename=People.xls");
            response.setContentType("application/vnd.ms-excel");
            new SimpleExporter().gridExport(headers, persons, "firstName, lastName, ", response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
        }
    }

    protected boolean isValid(final String login, final String password) {
        return new Authenticator().authenticate(login, password);
    }

}
