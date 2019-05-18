package de.josmer.dwdcdc.springboot.controller;

import de.josmer.dwdcdc.springboot.base.controller.Controller;
import de.josmer.dwdcdc.springboot.base.interfaces.IFileReader;
import de.josmer.dwdcdc.springboot.base.interfaces.IJwtToken;
import de.josmer.dwdcdc.springboot.base.interfaces.IUserBCrypt;
import de.josmer.dwdcdc.springboot.base.interfaces.IUserRepository;
import de.josmer.dwdcdc.springboot.base.web.WebCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController
public final class ViewController extends Controller {
    private final String loginHtml;
    private final String irrHtml;
    private final String radHtml;

    @Autowired
    public ViewController(IUserRepository userRep, IJwtToken jwtToken, IUserBCrypt userBCrypt, IFileReader fileReader) {
        super(userRep, jwtToken, userBCrypt);
        this.loginHtml = fileReader.asString("src/main/resources/static/html/login.html");
        this.irrHtml = fileReader.asString("src/main/resources/static/html/irr.html");
        this.radHtml = fileReader.asString("src/main/resources/static/html/rad.html");
        createUser("admin", "Super71212!");
        createUser("user", "abc123");
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String getHtml(HttpServletRequest req) {
        try {
            WebCookie webCookie = new WebCookie();
            setWebCookie(webCookie, req.getCookies());
            if (isAccess(webCookie.getToken())) {
                return chooseHtml(webCookie.getApp());
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return loginHtml;
    }

    private String chooseHtml(String app) {
        if (app.equals("irr")) {
            return irrHtml;
        } else {
            return radHtml;
        }
    }

    private void setWebCookie(WebCookie webCookie, Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                webCookie.setToken(cookie.getValue());
            }
            if (cookie.getName().equals("app")) {
                webCookie.setApp(cookie.getValue());
            }
        }
    }

    private void createUser(String username, String plainPassword) {
        if (userRep.get(username).isEmpty()) {
            userRep.createUser(username, plainPassword);
        }
    }
}
