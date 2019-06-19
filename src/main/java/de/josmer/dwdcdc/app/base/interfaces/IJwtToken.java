package de.josmer.dwdcdc.app.base.interfaces;

import io.jsonwebtoken.Claims;

public interface IJwtToken {
    Claims decode(String token);

    String create(String id, String issuer, String subject, long ttlMillis);

    String getSecretKey();
}
