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
        System.out.println("hi, Welcome on Galactic_Messenger !\n");

        while (!validChoice) {
            Scanner scanner = new Scanner(System.in);

            System.out.println("\u001B[34m" + "/help" +"\u001B[0m"+ " -> to see list of available commands" );

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
                        System.out.println("\u001B[32m" + "Registered successfully !" + "\u001B[0m");
                        ClientServices client = ClientServices.connectToServer(registerInfo, args);
                        if (client != null) {
                            ClientServices.startThreads(client);
                        }
                    }
                    validChoice = true;
                    break;

                case "/exit":
                    System.out.println("\u001B[32m" + "Good bye See you soon !" + "\u001B[0m");
                    validChoice = true;
                    break;

                case "/help":
                    System.out.println("\u001B[32m" + "List of available commands : " + "\u001B[0m" +"\n");

                    System.out.println("\u001B[37m" + "/login 'username' 'password' " + "\u001B[0m" + " -> Connexion\n" + "\u001B[37m"

                            + "/register 'username' 'password' "  + "\u001B[0m" + "-> Inscription\n"

                            + "\u001B[37m" +"/exit" +"\u001B[0m" + " -> Quitter \n");

                    break;

                default:
                    System.out.println("\u001B[31m" + "Invalid choice !" + "\u001B[0m");

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