package de.jos.dwdcdc.app.spring;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class EnvService {
  @Value("${DATABASE_URL:postgres://postgres:postgres@localhost:5432/db}")
  private String databaseUrl;

  @Value("${APP_SECRET:abc123}")
  private String appSecret;

  @Value("${APP_ADMIN_PASSWORD:admin}")
  private String appAdminPassword;
}
