package org.NilsCorentin.server;

import org.NilsCorentin.config.Config;
import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>(); // Garde une trace de tout les clients connectés pour que tout le monde est accées aux messages
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private String clientUsername;

    private String clientPassword;






    Config color = new Config();





    public ClientHandler(Socket socket){

        DbHandler db = new DbHandler();
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            this.clientPassword = bufferedReader.readLine();
            //db.insertClientsInfosinTable(clientUsername, clientPassword);
            clientHandlers.add(this);
            String clientJoinedMsg = "INFOS: " + clientUsername + " à rejoint le chat";
            String colorizedClientJoinedMsg = color.colorizeText(clientJoinedMsg, Config.PURPLE);


            List<String> connectedClients = ClientHandler.getConnectedClients();
            String cooloredConnectedClients = color.colorizeText(connectedClients.toString(), Config.YELLOW);
            broadcastMessage(colorizedClientJoinedMsg);
            broadcastMessage(Config.GREEN+ "connected users : "+ Config.RESET + cooloredConnectedClients);

        }catch (IOException err){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }









    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()){
            try {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            }catch (IOException err){
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public static List<String> getConnectedClients() {
        List<String> connectedClients = new ArrayList<>();
        for (ClientHandler clientHandler : clientHandlers) {
            connectedClients.add(clientHandler.clientUsername);
        }
        return connectedClients;
    }









    public void broadcastMessage(String messageToSend){
        for(ClientHandler clientHandler : clientHandlers){
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }catch (IOException err){
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }

    }

    public void broadcastSelfMessage(String messageToSend){
        for(ClientHandler clientHandler : clientHandlers){
            try {
                if (clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.bufferedWriter.write(color.BLUE + "moi: " +messageToSend+ color.RESET);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }catch (IOException err){
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
        String clientLeftMsg = "INFOS: " + clientUsername + " à quitté le chat";
        String colorizedClientLeftMsg = color.colorizeText(clientLeftMsg, Config.PURPLE);
        List<String> connectedClients = ClientHandler.getConnectedClients();
        broadcastMessage(colorizedClientLeftMsg + " | " + connectedClients);


    }









    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();
        try {
            if(bufferedReader != null){
                bufferedReader.close();
            }

            if (bufferedWriter != null){
                bufferedWriter.close();
            }

            if (socket != null){
                socket.close();
            }
        }catch (IOException err){
            err.printStackTrace();
        }
    }





}
