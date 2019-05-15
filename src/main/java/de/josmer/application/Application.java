package de.josmer.application;

import de.josmer.application.controller.security.Token;
import de.josmer.application.library.enums.RadTypes;
import de.josmer.application.library.handler.InsertHandler;
import de.josmer.application.library.handler.TokenHandler;
import de.josmer.application.library.interfaces.IUserRepository;
import de.josmer.application.model.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Arrays;

@Configuration
@SpringBootApplication
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        Token.init();
        createAdminUser();
        createDefaultUser();
        startHandler();
        openBrowser();
    }

    private static void createAdminUser() {
        IUserRepository userRepository = new UserRepository();
        if (userRepository.get("admin").isEmpty()) {
            userRepository.saveUser("admin", "Super71212!");
        }
    }

    private static void createDefaultUser() {
        IUserRepository userRepository = new UserRepository();
        if (userRepository.get("user").isEmpty()) {
            userRepository.saveUser("user", "abc123");
        }
    }

    private static void startHandler() {
        new InsertHandler(RadTypes.GLOBAL).start();
        new InsertHandler(RadTypes.DIFFUSE).start();
        new InsertHandler(RadTypes.DIRECT).start();
        new TokenHandler().start();
    }

    private static void openBrowser() {
        try {
            if (System.getenv("DEV_BROWSER") == null || System.getenv("DEV_URL") == null) {
                return;
            }
            new ProcessBuilder(System.getenv("DEV_BROWSER"), System.getenv("DEV_URL")).start();
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            LOGGER.info("Let's inspect the beans provided by Spring Boot:");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                LOGGER.info(beanName);
            }
        };
    }
}
