package org.example;


import org.example.Services.ClientServices;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.Scanner;

@SpringBootApplication
public class Main {


    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Bonjour, veuillez vous connecter (1) ou vous enregistrer (2) pour accéder au chat");
        int choice = Integer.parseInt(scanner.nextLine());

        if (choice == 1) {
            String[] userInfo = ClientServices.getUserInfo(scanner);
            if (userInfo != null) {
                ClientServices client = ClientServices.connectToServer(userInfo);
                if (client != null) {
                    ClientServices.startThreads(client);
                }
            }
        } else if (choice == 2) {
            String[] userInfo = ClientServices.getUserInfo(scanner);
            if (userInfo != null) {
                System.out.println("Enregistrement réussi !");
                ClientServices client = ClientServices.connectToServer(userInfo);
                if (client != null) {
                    ClientServices.startThreads(client);
                }
            }
        }
    }
}