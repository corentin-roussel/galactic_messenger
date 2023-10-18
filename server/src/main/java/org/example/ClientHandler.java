package org.example;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>(); // Garde une trace de tout les clients connectés pour que tout le monde est accées aux messages
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private String clientUsername;

    private String clientPassword;




    public void insertClientsInfosinTable(String clientUsername,String clientPassword){
        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:galactic_messenger.db")) {
            String query = "INSERT INTO clients (username, password) VALUES (?, ?)";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, clientUsername);
                preparedStatement.setString(2, clientPassword);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }







    public ClientHandler(Socket socket){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            this.clientPassword = bufferedReader.readLine();
            insertClientsInfosinTable(clientUsername, clientPassword);
            clientHandlers.add(this);
            broadcastMessage("INFOS: " + clientUsername + " à rejoint le chat");

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








    public void broadcastMessage(String messageToSend){
        for(ClientHandler clientHandler : clientHandlers){
            try {
                if ( !clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.bufferedWriter.write(messageToSend);
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
        broadcastMessage("INFOS: " + clientUsername + " à quitté le chat");
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
