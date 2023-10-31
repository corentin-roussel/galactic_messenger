package org.NilsCorentin.client;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.NilsCorentin.config.*;
import org.NilsCorentin.server.ClientHandler;

import static org.NilsCorentin.client.Client.*;

public class MainClient {


    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage: java -jar galactic_messenger_client.jar <adresse IP> <port>");
            System.exit(1); // Quitter le programme avec un code d'erreur
        }

        String ipArg = args[0];
        int portArg = Integer.parseInt(args[1]);
        connectToServer(args);
}}
