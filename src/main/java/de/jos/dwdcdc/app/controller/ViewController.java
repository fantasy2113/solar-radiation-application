package de.jos.dwdcdc.app.controller;

import de.jos.dwdcdc.app.entities.web.WebCookie;
import de.jos.dwdcdc.app.interfaces.IJwtToken;
import de.jos.dwdcdc.app.interfaces.IUserBCrypt;
import de.jos.dwdcdc.app.interfaces.IUserRepository;
import de.jos.dwdcdc.app.spring.EnvService;
import de.jos.dwdcdc.app.utils.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController
public final class ViewController extends Controller {

  private static final Logger LOGGER = LoggerFactory.getLogger(ViewController.class.getName());
  private final String irrHtml;
  private final String loginHtml;
  private final String radHtml;

  @Autowired
  public ViewController(IUserRepository userRep, IJwtToken jwtToken, IUserBCrypt userBCrypt, EnvService envService) {
    super(userRep, jwtToken, userBCrypt);
    this.loginHtml = Resource.get("static/html/login.html");
    this.irrHtml = Resource.get("static/html/irr.html");
    this.radHtml = Resource.get("static/html/rad.html");
    executeTask(() -> {
      createUser("admin", envService.getAppAdminPassword());
    });
  }

  private String chooseHtml(String app) {
    if (app.equals("irr")) {
      LOGGER.info("return - irr");
      return irrHtml;
    } else {
      LOGGER.info("return - rad");
      return radHtml;
    }
  }

  private void createUser(String username, String plainPassword) {
    if (userRep.get(username).isEmpty()) {
      LOGGER.info("Create user: " + username);
      userRep.createUser(username, plainPassword);
    }
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
      LOGGER.info(e.toString());
    }
    LOGGER.info("return - login");
    return loginHtml;
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
}
