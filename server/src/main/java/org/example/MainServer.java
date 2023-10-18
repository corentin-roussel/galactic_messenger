package org.example;

import org.example.Services.*;
import java.io.IOException;
import java.net.ServerSocket;

public class MainServer {


    public static void MainServer(String[] args) throws IOException {
        String hostname = args[0];
        ServerSocket ss = new ServerSocket(Integer.parseInt(args[1]));

        ServerService server = new ServerService(hostname, ss);

        server.startServer();
    }


}
