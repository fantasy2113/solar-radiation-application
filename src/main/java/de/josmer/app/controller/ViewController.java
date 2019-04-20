package de.josmer.app.controller;

import de.josmer.app.library.interfaces.*;
import de.josmer.app.library.security.Token;
import de.josmer.app.library.utils.Toolbox;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ViewController extends AController {

    @Autowired
    ViewController(IExportRadiRepository exportRadiRepo, IExportCalcRepository exportCalcRepo, IRadiationRepository radiRepo, IUserRepository userRepo, ICalculatedRepository calcRepo) {
        super(exportRadiRepo, exportCalcRepo, radiRepo, userRepo, calcRepo);
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String getView(HttpServletRequest req) {
        try {
            Cookie[] cookies = req.getCookies();
            String token = "";
            String path = "";
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                }
                if (cookie.getName().equals("app")) {
                    path = cookie.getValue();
                }
            }
            if (isAccess(Token.getAuthentication(token))) {
                if (path.equals("calcapp")) {
                    return Toolbox.readFile("src/main/resources/static/html/calc.html");
                }
                return Toolbox.readFile("src/main/resources/static/html/radi.html");
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return Toolbox.readFile(LOGIN_HTML);
    }
}
