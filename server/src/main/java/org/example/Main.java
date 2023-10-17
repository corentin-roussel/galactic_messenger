package org.example;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
@EnableConfigurationProperties
@EntityScan(basePackages = {"com.acme.model"})
public class Main {
    private static ConfigurableApplicationContext applicationContext;
    public static void main(String[] args) {
        Main.applicationContext = SpringApplication.run(Main.class, args);


    }
}