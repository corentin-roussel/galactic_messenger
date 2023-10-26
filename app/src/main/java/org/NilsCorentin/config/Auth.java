package org.NilsCorentin.config;
import org.NilsCorentin.client.Client;
import org.NilsCorentin.config.Config;
import org.NilsCorentin.server.DbHandler;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Scanner;

import static org.NilsCorentin.client.Client.*;

public class Auth {

    public static void init() {
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
                    Auth.login();
                    validChoice = true;
                    break;

                case "/register":
                    Auth.register();
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

    public static void login() {
        boolean validCred = false;
        DbHandler dbHandler = new DbHandler();

        while (!validCred) {
            Scanner scanner = new Scanner(System.in);
            String[] loginInfo = getUserInfo(scanner);


            String hashedPswdfromDb = dbHandler.getHashedPassword(loginInfo[0]);

            String user = dbHandler.getUserByName(loginInfo[0]);
            if (user == null) {
                System.out.println(Config.colorizeText("User doesn't exist", Config.RED));
                validCred = false;
            }else if (hashedPswdfromDb != null && BCrypt.checkpw(loginInfo[1], hashedPswdfromDb)) {
                System.out.println(Config.colorizeText("User logged in successfully!", Config.GREEN));
                Client client = connectToServer(loginInfo);
                startThreads(client);
                validCred = true;
            } else {
                System.out.println(Config.colorizeText("Invalid credentials" +
                        "", Config.RED));
                validCred = false;
            }
        }
    }

    public static void register() {
        boolean validCred = false;
        DbHandler dbHandler = new DbHandler();

        while (!validCred) {
            Scanner scanner = new Scanner(System.in);
            String[] loginInfo = getUserInfo(scanner);
            if (dbHandler.getUserByName(loginInfo[0]) == null) {
                System.out.println(loginInfo[0]);
                System.out.println(loginInfo[1]);
                dbHandler.insertClientsInfosinTable(loginInfo[0], loginInfo[1]);

                System.out.println(Config.colorizeText("User created successfully!", Config.GREEN));
                init();
                //Client client = connectToServer(loginInfo);
                //startThreads(client);
                validCred = true;
            } else {
                System.out.println(loginInfo[0]);
                System.out.println(loginInfo[1]);
                System.out.println(Config.colorizeText("User already exists!", Config.RED));
                validCred = false;
            }
        }
    }
}
