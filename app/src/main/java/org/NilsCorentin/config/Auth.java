package org.NilsCorentin.config;
import org.NilsCorentin.client.Client;

import org.NilsCorentin.server.ClientHandler;
import org.NilsCorentin.server.DbHandler;
import org.mindrot.jbcrypt.BCrypt;

import java.net.Socket;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Scanner;

import static org.NilsCorentin.client.Client.*;

public class Auth {

    public static void init(String[] args) {
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
            String[] words = choice.split(" ");
            System.out.println(Arrays.toString(words));
            switch (words[0]) {
                case "/login":
                    Auth.login(words, args);
                    validChoice = true;
                    break;

                case "/register":
                    Auth.register(words, args);
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

    public static void login(String[] infoLogin, String[] args) {
        boolean validCred = false;
        DbHandler dbHandler = new DbHandler();

        while (!validCred) {



            String hashedPswdfromDb = dbHandler.getHashedPassword(infoLogin[1]);

            String user = dbHandler.getUserByName(infoLogin[1]);
            if (user == null) {
                System.out.println(Config.colorizeText("User doesn't exist", Config.RED));
                init(args);
                validCred = false;
            }else if (hashedPswdfromDb != null && BCrypt.checkpw(infoLogin[2], hashedPswdfromDb)) {
                Client client = connectToServer(infoLogin,args);
                if(client == null) {
                    System.out.println("Wrong ip or port number please be sure that you entered the right information");
                    System.exit(0);
                }
                startThreads(client);
                System.out.println(Config.colorizeText("User logged in successfully!", Config.GREEN));
                validCred = true;
            } else {
                System.out.println(Config.colorizeText("Invalid credentials" +
                        "", Config.RED));
                init(args);
                validCred = false;
            }
        }
    }

    public static void register(String[] infoLogin, String[] args) {
        boolean validCred = false;
        DbHandler dbHandler = new DbHandler();

        while (!validCred) {

            if (dbHandler.getUserByName(infoLogin[1]) == null) {
                System.out.println(infoLogin[1]);
                System.out.println(infoLogin[2]);
                dbHandler.insertClientsInfosinTable(infoLogin[1], infoLogin[2]);

                System.out.println(Config.colorizeText("User created successfully!", Config.GREEN));
                init(args);
                validCred = true;
            } else {
                System.out.println(infoLogin[1]);
                System.out.println(infoLogin[2]);
                System.out.println(Config.colorizeText("User already exists!", Config.RED));
                init(args);
                validCred = false;
            }
        }
    }
}
