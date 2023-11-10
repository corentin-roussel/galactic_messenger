package org.NilsCorentin.server;

import org.NilsCorentin.server.*;
import org.NilsCorentin.config.Config;

import java.net.*;
import java.sql.*;
import java.util.Hashtable;
import java.io.*;
import java.net.InetAddress;

public class Server {

    private ServerSocket ss;


    public Server(ServerSocket ss){

        this.ss = ss;
    }








    public void startServer(){
        Config color = new Config();

        try {
            InetAddress ip = InetAddress.getLocalHost();
            String hostAddress = ip.getHostAddress();
            String showHostAdress= "Adresse ip du serveur : " + hostAddress;
            System.out.println(color.colorizeText(showHostAdress, Config.WHITE));

            while(!ss.isClosed()){
                Socket socket = ss.accept();
                String newConn = "Nouvelle connection !";
                System.out.println(color.colorizeText(newConn, Config.GREEN));
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();

            }
        }catch (IOException err){
            err.printStackTrace();
        }
    }


















    public static void main(String[] args) throws IOException {
        DbHandler db = new DbHandler();

        ServerSocket ss = new ServerSocket(Integer.parseInt(args[0]));
        Server server = new Server(ss);
        server.startServer();
        db.createTables();





    }}

