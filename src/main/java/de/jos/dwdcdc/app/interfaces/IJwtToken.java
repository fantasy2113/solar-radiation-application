package de.jos.dwdcdc.app.interfaces;

import io.jsonwebtoken.Claims;

public interface IJwtToken {

  String create(String id, String issuer, String subject, long ttlMillis);

  Claims decode(String token);

  String getSecretKey();
}
