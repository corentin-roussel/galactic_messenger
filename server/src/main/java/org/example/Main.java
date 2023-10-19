package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableConfigurationProperties
@EntityScan(basePackages = {"org.example.Model"})
@ComponentScan("org.example.Repository")
public class Main {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(Main.class, args);

        MainServer.MainServer(args);

    }
}