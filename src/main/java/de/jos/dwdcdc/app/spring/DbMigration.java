package de.jos.dwdcdc.app.spring;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbMigration {
  @Autowired
  public DbMigration(Flyway flyway) {
    flyway.migrate();
  }
}
