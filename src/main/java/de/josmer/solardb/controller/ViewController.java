package de.josmer.solardb.controller;

import de.josmer.solardb.controller.security.JwtToken;
import de.josmer.solardb.repositories.UserRepository;
import de.josmer.solardb.utils.FileReader;
import de.josmer.solardb.utils.UserBCrypt;
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
    public ViewController(UserRepository userRep, JwtToken jwtToken, UserBCrypt userBCrypt, FileReader fileReader) {
        super(userRep, jwtToken, userBCrypt);
        this.loginHtml = fileReader.asString("src/main/resources/static/html/login.html");
        this.irrHtml = fileReader.asString("src/main/resources/static/html/irr.html");
        this.radHtml = fileReader.asString("src/main/resources/static/html/rad.html");
        createUser("admin", "Super71212!");
        createUser("user", "abc123");
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String getView(HttpServletRequest req) {
        try {
            Cookie[] cookies = req.getCookies();
            String token = "";
            String app = "";
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                }
                if (cookie.getName().equals("app")) {
                    app = cookie.getValue();
                }
            }
            if (isAccess(token)) {
                if (app.equals("irr")) {
                    return irrHtml;
                } else {
                    return radHtml;
                }
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return loginHtml;
    }

    private void createUser(String username, String plainPassword) {
        if (userRep.get(username).isEmpty()) {
            userRep.createUser(username, plainPassword);
        }
    }
}
