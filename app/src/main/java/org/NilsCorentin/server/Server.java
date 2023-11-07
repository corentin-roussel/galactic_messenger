package org.NilsCorentin.server;

import org.NilsCorentin.server.*;
import org.NilsCorentin.config.Config;

import java.net.*;
import java.sql.*;
import java.util.Hashtable;
import java.io.*;
import java.net.InetAddress;

public class Server {











    public void startServer(String[] args){
        Config color = new Config();

        try {

            InetAddress ip = InetAddress.getLocalHost();
            String hostAddress = ip.getHostAddress();
            InetSocketAddress  inetSocketAddress = new InetSocketAddress(hostAddress, Integer.parseInt(args[0]));
            ServerSocket ss = new ServerSocket();
            ss.bind(inetSocketAddress);
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
        Server server = new Server();
        server.startServer(args);
        db.createTables();
        }}

