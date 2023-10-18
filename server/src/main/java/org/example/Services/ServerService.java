package org.example.Services;

import java.net.*;
import java.sql.*;
import java.util.Hashtable;
import java.io.*;
import java.net.InetAddress;

public class ServerService {

    private ServerSocket ss;
    private String serverIp;

    public ServerService(String serverIp, ServerSocket ss){
        this.serverIp = serverIp;
        this.ss = ss;
    }

    public void startServer(){

        try {
            String hostAddress = serverIp;
            System.out.println("Adresse ip du serveur : " + hostAddress +":"+ ss.getLocalPort());

            while(!ss.isClosed()){
                Socket socket = ss.accept();
                System.out.println("Nouvelle connection !" );

            }
        }catch (IOException err){
            err.printStackTrace();
        }
    }
}
