package org.example;


import org.example.Services.ClientServices;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.Scanner;

@SpringBootApplication
public class Main {


    public static void main(String[] args) {
        Boolean validChoice = false;


        while (!validChoice) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Bonjour, voulez-vous vous connecter ou vous inscrire ?\n\n" +"\u001B[34m" + "/login" + "\u001B[0m" + " -> Connexion\n" + "\u001B[34m" + "/register"  + "\u001B[0m" + "-> Inscription\n" + "\u001B[34m" +"/exit" +"\u001B[0m" + " -> Quitter \n");
            String choice = scanner.nextLine();
            switch (choice) {
                case "/login":
                    String[] loginInfo = ClientServices.getUserInfo(scanner);
                    if (loginInfo != null) {
                        ClientServices client = ClientServices.connectToServer(loginInfo);
                        if (client != null) {
                            ClientServices.startThreads(client);
                        }
                    }
                    validChoice = true;
                    break;

                case "/register":
                    String[] registerInfo = ClientServices.getUserInfo(scanner);
                    if (registerInfo != null) {
                        System.out.println("Enregistrement réussi !");
                        ClientServices client = ClientServices.connectToServer(registerInfo);
                        if (client != null) {
                            ClientServices.startThreads(client);
                        }
                    }
                    validChoice = true;
                    break;

                case "/exit":
                    System.out.println("Aurevoir !");
                    validChoice = true;
                    break;

                default:
                    System.out.println("Choix invalide.");

                    break;
            }
        }
    }
}

/*public void shutServer(){
            try {
                if (ss != null){
                    ss.close();
                }
            }catch (IOException err){
                err.printStackTrace();
            }
    }*/