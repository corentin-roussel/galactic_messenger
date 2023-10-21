package org.example;

import org.example.Controller.UserController;
import org.example.Model.UserModel;
import org.example.Repository.UserRepository;
import org.example.Services.*;
import java.io.IOException;
import java.net.ServerSocket;

public class MainServer  {


    public static void MainServer(String[] args) throws IOException  {

        ServerSocket ss = new ServerSocket(Integer.parseInt(args[0]));
        ServerService server = new ServerService(ss);
        server.initDb();
        server.startServer();
    }


}
