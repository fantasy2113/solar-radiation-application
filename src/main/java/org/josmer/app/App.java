package org.josmer.app;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.josmer.app.handler.InsertHandler;
import org.josmer.app.repository.RadiationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        if (!new RadiationRepository().isConnected()) {
            System.out.println("no db connection");
            return;
        }
        SpringApplication.run(App.class, args);

        openBrowser();

        if (true) {
            ExecutorService pool = Executors.newFixedThreadPool(1);
            pool.execute(new InsertHandler(new RadiationRepository()));
        }
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

        };
    }

    private static void openBrowser() {
        try {
            new ProcessBuilder(System.getenv("DEV_BROWSER"), System.getenv("DEV_URL")).start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
