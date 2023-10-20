package org.example.Services;

import java.net.*;
import java.sql.*;
import java.io.*;
import java.net.InetAddress;

public class ServerService{

    private static ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public ServerService(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }


    public void initDb(){
        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:galactic_messenger.db");
            Statement statement = connection.createStatement()) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT NOT NULL," +
                    "password TEXT NOT NULL)";
            statement.executeUpdate(createTableSQL);
            System.out.println("Table crée avec succées");
        }catch (SQLException err){
            err.printStackTrace();
        }
    }

    public void startServer(){

        try {
            String hostAdress = "127.0.0.1";
            System.out.println("Adresse ip du serveur : " + hostAdress +":"+ serverSocket.getLocalPort());


            while(!serverSocket.isClosed()) {
                clientSocket = serverSocket.accept();
                System.out.println("Nouvelle connection !" );
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            }


        }catch (IOException err){
            err.printStackTrace();
        }
    }
}
