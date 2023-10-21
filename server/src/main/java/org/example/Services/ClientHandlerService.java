package org.example.Services;

import org.mindrot.jbcrypt.BCrypt;

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
            for (ClientHandlerService clientHandler : clientHandlers) {
                if (!clientHandler.userUsername.equals(userUsername)) {
                    broadcastMessage("INFOS: " + userUsername + " à rejoint le chat en feu");
                }else broadcastSelfMessage("INFOS: Vous avez rejoint le chat vous pouvez commencer à discuter avec les autres personnes connectées");
            }


        }catch (IOException err){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public static String hashPassword(String password) {
        // Générer un salt BCrypt sécurisé
        String salt = BCrypt.gensalt();

        // Hacher le mot de passe avec le salt
        String hashedPassword = BCrypt.hashpw(password, salt);

        return hashedPassword;
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        // Vérifier si le mot de passe correspond au hachage stocké
        return BCrypt.checkpw(password, hashedPassword);
    }

    public void insertClientsInfosInTable(String userUsername, String userPassword) {
        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:galactic_messenger.db")) {
            String hashedPassword = hashPassword(userPassword);
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, userUsername);
                preparedStatement.setString(2, hashedPassword);
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


            } catch (IOException err) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void  broadcastSelfMessage(String messageToSend) {
        for (ClientHandlerService clientHandler : clientHandlers) {
            try {
                if (clientHandler.userUsername.equals(userUsername)) {
                    clientHandler.bufferedWriter.write("moi: " + messageToSend);
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
