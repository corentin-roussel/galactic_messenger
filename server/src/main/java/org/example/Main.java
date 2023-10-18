package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.IOException;

@SpringBootApplication
@EnableConfigurationProperties
@EntityScan(basePackages = {"org.example.*"})
public class Main {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(Main.class, args);

        MainServer.MainServer(args);

    }
}