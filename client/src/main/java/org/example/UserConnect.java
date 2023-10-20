package org.example;


import org.aspectj.lang.annotation.Before;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserConnect {

    private Socket clientSocket;

    private String ip;
    private int port;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);


        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String sendMessage(String msg) throws IOException {
        out.println(msg);
        return in.readLine();
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public static void userConnection(String[] args) throws IOException {
        UserConnect userConnect = new UserConnect();

        String ipServer = args[0];
        int portServer = Integer.parseInt(args[1]);

        userConnect.startConnection(ipServer,portServer);

    }






}
