package de.orbis.application.controller.v1;

import de.orbis.application.controller.v1.security.JwtToken;
import de.orbis.application.controller.v1.security.WebToken;
import de.orbis.application.model.entity.User;
import de.orbis.application.model.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/v1")
public final class TokenController {
    private static final Long TTL_MILLIS = TimeUnit.DAYS.toMillis(5);
    private final JwtToken jwtToken;
    private final UserRepository userRepository;

    @Autowired
    public TokenController(JwtToken jwtToken, UserRepository userRepository) {
        this.jwtToken = jwtToken;
        this.userRepository = userRepository;
        this.userRepository.createUser("admin", "qw999");
    }

    @GetMapping("/token")
    public ResponseEntity<WebToken> token(@RequestHeader("username") final String username, @RequestHeader("password") final String password, @RequestHeader("app") final String app) {
        return userRepository.get(username).map(user -> new ResponseEntity<>(createWebToken(user, app), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(new WebToken(), HttpStatus.UNAUTHORIZED));
    }

    @GetMapping("/verify_access")
    public ResponseEntity<Integer> verifyAccess(@RequestHeader("token") final String token) {
        final Claims decodedToken = jwtToken.decode(token);
        return userRepository.get(getUserId(decodedToken)).map(user -> new ResponseEntity<>(user.getId(), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(-1, HttpStatus.UNAUTHORIZED));
    }

    @GetMapping("/secret")
    public ResponseEntity<String> secret(@RequestHeader("token") final String token) {
        final Claims decodedToken = jwtToken.decode(token);
        return userRepository.get(getUserId(decodedToken)).map(user -> new ResponseEntity<>(jwtToken.getSecretKey(), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>("", HttpStatus.UNAUTHORIZED));
    }

    private int getUserId(Claims decodedToken) {
        return Integer.parseInt(decodedToken.getId());
    }

    private WebToken createWebToken(User user, String app) {
        WebToken webToken = new WebToken();
        webToken.setToken(jwtToken.create(String.valueOf(user.getId()), app, user.getUserEmail(), TTL_MILLIS));
        webToken.setSecret(jwtToken.getSecretKey());
        return webToken;
    }
}
