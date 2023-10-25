package org.NilsCorentin.client;

import java.io.IOException;
import java.util.Scanner;

import org.NilsCorentin.config.Config;

import static org.NilsCorentin.client.Client.*;

public class MainClient {


    public static void main(String[] args) {
        String msg = " ";
        String colorizedMsg = " ";
        String appName = "Galactic_Messenger";
        String colorizedAppName = Config.colorizeText(appName, Config.BLUE);
        System.out.println("Welcome on " + colorizedAppName);

        Boolean validChoice = false;


        while (!validChoice) {
            Scanner scanner = new Scanner(System.in);
            msg = "/help -> to see list of available commands";
            colorizedMsg = Config.colorizeText(msg, Config.BLUE);
            System.out.println(colorizedMsg);

            String choice = scanner.nextLine();
            switch (choice) {
                case "/login":
                    String[] loginInfo = getUserInfo(scanner);
                    if (loginInfo != null) {
                        Client client = connectToServer(loginInfo);
                        if (client != null) {
                            startThreads(client);
                        }
                    }
                    validChoice = true;
                    break;

                case "/register":
                    String[] registerInfo = getUserInfo(scanner);
                    if (registerInfo != null) {
                        msg = "Registered successfully !";
                        colorizedMsg = Config.colorizeText(msg, Config.GREEN);
                        System.out.println(colorizedMsg);
                        Client client = connectToServer(registerInfo);
                        if (client != null) {
                            startThreads(client);
                        }
                    }
                    validChoice = true;
                    break;

                case "/exit":
                    msg = "Good bye See you soon !";
                    colorizedMsg = Config.colorizeText(msg, Config.GREEN);
                    System.out.println(colorizedMsg);
                    validChoice = true;
                    break;

                case "/help":
                    msg = "List of available commands : ";
                    colorizedMsg = Config.colorizeText(msg, Config.BLUE);
                    System.out.println(colorizedMsg);

                    msg = "/login 'username' 'password' -> Connexion\n";
                    String msg2 = "/register 'username' 'password' -> Inscription\n";
                    String msg3 = "/exit -> Quitter \n";

                    colorizedMsg = Config.colorizeText(msg, Config.WHITE);
                    String colorizedMsg2 = Config.colorizeText(msg2, Config.WHITE);
                    String colorizedMsg3 = Config.colorizeText(msg3, Config.WHITE);
                    System.out.println(colorizedMsg + colorizedMsg2 + colorizedMsg3);

                    break;

                default:
                    msg = "Invalid choice !";
                    colorizedMsg = Config.colorizeText(msg, Config.RED);
                    System.out.println(colorizedMsg);

                    break;
            }
        }
    }
}
