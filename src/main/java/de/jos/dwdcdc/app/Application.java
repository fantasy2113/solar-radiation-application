package de.jos.dwdcdc.app;

import de.jos.dwdcdc.app.repositories.SolRadRepository;
import de.jos.dwdcdc.app.spring.AppBeans;
import de.jos.dwdcdc.app.spring.AppContext;
import de.jos.dwdcdc.app.utils.FileReader;
import de.jos.dwdcdc.library.handler.SolRadInsertAllHandler;
import de.jos.dwdcdc.library.handler.SolRadInsertAtMidnightHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
