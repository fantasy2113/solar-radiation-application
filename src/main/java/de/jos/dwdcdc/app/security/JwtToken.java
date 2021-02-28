package de.jos.dwdcdc.app.security;

import de.jos.dwdcdc.app.interfaces.IJwtToken;
import de.jos.dwdcdc.app.spring.EnvService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Component
public final class JwtToken implements IJwtToken {

  private final String secretKey;

  @Autowired
  public JwtToken(EnvService envService) {
    this.secretKey = envService.getAppSecret();
  }

  @Override
  public String create(String id, String issuer, String subject, long ttlMillis) {
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    long nowMillis = System.currentTimeMillis();
    Date now = new Date(nowMillis);
    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
    JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(now).setSubject(subject).setIssuer(issuer)
        .signWith(signatureAlgorithm, signingKey);
    if (ttlMillis >= 0) {
      long expMillis = nowMillis + ttlMillis;
      Date exp = new Date(expMillis);
      builder.setExpiration(exp);
    }
    return builder.compact();
  }

  @Override
  public Claims decode(String token) {
    return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey)).parseClaimsJws(token)
        .getBody();
  }

  @Override
  public String getSecretKey() {
    return secretKey;
  }
}
