package org.example.Services;

import java.net.*;
import java.sql.*;
import java.util.Hashtable;
import java.io.*;
import java.net.InetAddress;

public class ServerService {

    private ServerSocket ss;

    public ServerService(ServerSocket ss){
        this.ss = ss;
    }


    public void initDb(){
        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:galactic_messenger.db");
            Statement statement = connection.createStatement()) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT NOT NULL," +
                    "password TEXT NOT NULL)";
            statement.executeUpdate(createTableSQL);
            System.out.println("table crée avec succées");

        }catch (SQLException err){
            err.printStackTrace();
        }
    }
    public void startServer(){

        try {
            InetAddress ip = InetAddress.getLocalHost();
            String hostAdress = ip.getHostAddress();
            System.out.println("Adresse ip du serveur : " + hostAdress +":"+ ss.getLocalPort());

            while(!ss.isClosed()){
                Socket socket = ss.accept();
                System.out.println("Nouvelle connection !" );

            }
        }catch (IOException err){
            err.printStackTrace();
        }
    }
}
