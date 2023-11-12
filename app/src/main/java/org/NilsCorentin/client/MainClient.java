package org.NilsCorentin.client;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.NilsCorentin.config.*;
import org.NilsCorentin.server.ClientHandler;

import static org.NilsCorentin.client.Client.*;

public class MainClient {


    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java -jar client.jar <ip> <port>");
            System.exit(1);
        }
        Auth.init(Integer.parseInt(args[1]));

    }
}
