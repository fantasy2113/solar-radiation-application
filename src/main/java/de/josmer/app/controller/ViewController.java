package de.josmer.app.controller;

import de.josmer.app.controller.security.Token;
import de.josmer.app.library.interfaces.IUserRepository;
import de.josmer.app.library.utils.Toolbox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController
public class ViewController extends Controller {
    private static final String LOGIN_HTML = Toolbox.readFile("src/main/resources/static/html/login.html");
    private static final String IRR_HTML = Toolbox.readFile("src/main/resources/static/html/irr.html");
    private static final String RAD_HTML = Toolbox.readFile("src/main/resources/static/html/rad.html");

    @Autowired
    public ViewController(IUserRepository userRep) {
        super(userRep);
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
            if (isAccess(Token.getAuthentication(token))) {
                if (app.equals("irr")) {
                    return IRR_HTML;
                } else {
                    return RAD_HTML;
                }
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return LOGIN_HTML;
    }
}
