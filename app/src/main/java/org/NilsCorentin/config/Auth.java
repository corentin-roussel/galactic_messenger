package org.NilsCorentin.config;
import org.NilsCorentin.client.Client;
import org.NilsCorentin.config.Config;
import org.NilsCorentin.server.DbHandler;

import java.util.Scanner;

import static org.NilsCorentin.client.Client.*;

public class Auth {






    public static void login() {

        DbHandler dbHandler = new DbHandler();
        Scanner scanner = new Scanner(System.in);
        String[] loginInfo = getUserInfo(scanner);

        if (dbHandler.getUserByName(loginInfo[0]) == null) {
            System.out.println(loginInfo[0]);
            System.out.println(loginInfo[1]);
            System.out.println(Config.colorizeText("User does not exist!", Config.RED));
        }
        if (dbHandler.getHashedPassword(loginInfo[0]).equals(loginInfo[1])) {
            dbHandler.getHashedPassword(loginInfo[0]);
            System.out.println(loginInfo[0]);
            System.out.println(loginInfo[1]);
            System.out.println(Config.colorizeText("Logged in successfully!", Config.GREEN));
            Client client = connectToServer(loginInfo);
            startThreads(client);
        } else {
            System.out.println(loginInfo[0]);
            System.out.println(loginInfo[1]);
            System.out.println(Config.colorizeText("Invalid credentials!", Config.RED));
        }
    }


    public static void register() {
        DbHandler dbHandler = new DbHandler();
        Scanner scanner = new Scanner(System.in);
        String[] loginInfo = getUserInfo(scanner);
        if (dbHandler.getUserByName(loginInfo[0]) == null) {
            System.out.println(loginInfo[0]);
            System.out.println(loginInfo[1]);
            dbHandler.insertClientsInfosinTable(loginInfo[0], loginInfo[1]);

            System.out.println(Config.colorizeText("User created successfully!", Config.GREEN));
            System.out.println(loginInfo[0]);
            System.out.println(loginInfo[1]);
            Client client = connectToServer(loginInfo);
            startThreads(client);
        } else {
            System.out.println(loginInfo[0]);
            System.out.println(loginInfo[1]);
            System.out.println(Config.colorizeText("User already exists!", Config.RED));
        }
    }
}
