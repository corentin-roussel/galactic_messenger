package org.example.Services;

import org.example.Model.UserModel;
import org.example.Repository.UserRepository;

import java.net.*;
import java.sql.*;
import java.io.*;
import java.net.InetAddress;

public class ServerService {

    private ServerSocket ss;

    public ServerService(ServerSocket ss){
        this.ss = ss;
    }



    public void initDb() {

        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:galactic_messenger.db");
            Statement statement = connection.createStatement()) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT NOT NULL," +
                    "password TEXT NOT NULL)";
            statement.executeUpdate(createTableSQL);
            System.out.println("\u001B[32m" + "Table crée avec succées !" + "\u001B[0m");

        }catch (SQLException err){
            err.printStackTrace();
        }
    }
    public void startServer(){

        try {
            InetAddress ip = InetAddress.getLocalHost();
            String hostAdress = "127.0.0.1";
            System.out.println("Adresse ip du serveur : " + hostAdress +":"+ ss.getLocalPort());


            while(!ss.isClosed()){
                Socket socket = ss.accept();
                System.out.println("\u001B[32m" + "Nouvel connection !" + "\u001B[0m");
                ClientHandlerService clientHandlerService =new ClientHandlerService(socket);
                Thread thread = new Thread(clientHandlerService);
                thread.start();
            }
        }catch (IOException err){
            err.printStackTrace();
        }
    }
}
