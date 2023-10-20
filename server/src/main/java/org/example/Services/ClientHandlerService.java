package org.example.Services;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientHandlerService implements Runnable{

    public UserService userService;
    public static ArrayList<ClientHandlerService> clientHandlers = new ArrayList<>(); // !!!!! PEUT ETRE CHANGER CA EN HASMAP POUR L'AUTHENTIFICATION !!!!! Garde une trace de tout les clients connectés pour que tout le monde est accées aux messages
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private String userUsername;

    private String userPassword;


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


    public ClientHandlerService(Socket socket){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userUsername = bufferedReader.readLine();
            this.userPassword = bufferedReader.readLine();
            insertClientsInfosInTable(userUsername, userPassword);
            clientHandlers.add(this);
            broadcastMessage("INFOS: " + userUsername + " à rejoint le chat en feu");

        }catch (IOException err){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void insertClientsInfosInTable(String userUsername, String userPassword) {
        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:galactic_messenger.db")) {
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, userUsername);
                preparedStatement.setString(2, userPassword);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void broadcastMessage(String messageToSend) { // SEPARER LA METHODE EN DEUX BROADCASTMESSAGE ET BROADCASTSELFMESSAGE
        for (ClientHandlerService clientHandler : clientHandlers) {
            try {
                if (!clientHandler.userUsername.equals(userUsername)) {
                    clientHandler.bufferedWriter.write(userUsername + ": " +messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }

                if (clientHandler.userUsername.equals(userUsername)){
                    clientHandler.bufferedWriter.write("moi: " +messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();

                }
            } catch (IOException err) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
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

    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("INFOS: " + userUsername + " à quitté le chat");
    }


}
