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

import java.util.Arrays;

@Configuration
@SpringBootApplication
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        Token.init();
        createUser("admin", "Super71212!");
        createUser("user", "abc123");
        startHandler();
    }

    private static void createUser(String username, String plainPassword) {
        IUserRepository userRepository = new UserRepository();
        if (userRepository.get(username).isEmpty()) {
            userRepository.saveUser(username, plainPassword);
        }
    }

    private static void startHandler() {
        new InsertHandler(RadTypes.GLOBAL).start();
        new InsertHandler(RadTypes.DIFFUSE).start();
        new InsertHandler(RadTypes.DIRECT).start();
        new TokenHandler().start();
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
