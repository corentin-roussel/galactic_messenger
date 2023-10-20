package org.example.Services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
 public class EchoClientHandler extends Thread {
    private final Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public EchoClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String inputLine;
        while (true) {
            try {
                if ((inputLine = in.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if ("exit".equals(inputLine)) {
                out.println("bye");
                break;
            }
            out.println(inputLine);
        }

        try {
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        out.close();
        try {
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}