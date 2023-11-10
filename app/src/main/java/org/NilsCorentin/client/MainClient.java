package org.NilsCorentin.client;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.NilsCorentin.config.*;
import org.NilsCorentin.server.ClientHandler;

import static org.NilsCorentin.client.Client.*;

public class MainClient {


    public static void main(String[] args) {
        try{
                Auth.init(args);
        }catch(IndexOutOfBoundsException e) {
            System.out.println("erreur: " + e);
            System.out.println("Verify that you have entered the number port and ip when launching the client");
        }


    }
}
