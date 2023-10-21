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

            System.out.println("Bonjour, voulez-vous vous connecter ou vous inscrire ?\n\n"

                    +"\u001B[34m" + "/login 'username' 'password' " + "\u001B[0m" + " -> Connexion\n" + "\u001B[34m"

                    + "/register 'username' 'password' "  + "\u001B[0m" + "-> Inscription\n"

                    + "\u001B[34m" +"/exit" +"\u001B[0m" + " -> Quitter \n"

                    + "\u001B[34m" + "/help" +"\u001B[0m"+ " -> to see list of available commands" );

            String choice = scanner.nextLine();
            switch (choice) {
                case "/login":
                    String[] loginInfo = ClientServices.getUserInfo(scanner);
                    if (loginInfo != null) {
                        ClientServices client = ClientServices.connectToServer(loginInfo,  args);
                        if (client != null) {
                            ClientServices.startThreads(client);
                        }
                    }
                    validChoice = true;
                    break;

                case "/register":
                    String[] registerInfo = ClientServices.getUserInfo(scanner);
                    if (registerInfo != null) {
                        System.out.println("\u001B[32m" + "Enregistrement réussi !" + "\u001B[0m");
                        ClientServices client = ClientServices.connectToServer(registerInfo, args);
                        if (client != null) {
                            ClientServices.startThreads(client);
                        }
                    }
                    validChoice = true;
                    break;

                case "/exit":
                    System.out.println("\u001B[32m" + "Aurevoir !" + "\u001B[0m");
                    validChoice = true;
                    break;

                default:
                    System.out.println("\u001B[31m" + "Choix invalide!" + "\u001B[0m");

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