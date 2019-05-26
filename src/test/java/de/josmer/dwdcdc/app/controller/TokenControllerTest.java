package de.josmer.dwdcdc.app.controller;

import de.josmer.dwdcdc.app.entities.User;
import de.josmer.dwdcdc.app.interfaces.IJwtToken;
import de.josmer.dwdcdc.app.interfaces.IUserRepository;
import io.jsonwebtoken.Claims;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;
import java.util.Optional;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TokenControllerTest {

    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate template;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IJwtToken jwtToken;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/token");
    }

    @Test
    public void getToken() throws Exception {
        Optional<User> optionalUser = userRepository.get("admin");
        HttpHeaders headers = new HttpHeaders();
        headers.add("login", "admin");
        headers.add("password", "Super71212!");
        final String token = jwtToken.create(String.valueOf(optionalUser.get().getId()), "sol", optionalUser.get().getUsername(), Controller.TTL_MILLIS);
        ResponseEntity<String> response = template.exchange(base.toString(), HttpMethod.GET, new HttpEntity<>(headers), String.class);
        Claims decode = jwtToken.decode(response.getBody());

        assertEquals(optionalUser.get().getId(), (int) Integer.valueOf(decode.getId()));
        assertEquals(optionalUser.get().getUsername(), decode.getSubject());
    }
}