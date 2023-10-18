package org.example.Services;

import org.hibernate.HibernateException;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientHandlerService implements Runnable{

    public UserService userService;
    public static ArrayList<ClientHandlerService> clientHandlers = new ArrayList<>(); // Garde une trace de tout les clients connectés pour que tout le monde est accées aux messages
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private String userUsername;

    private String userPassword;

    public ClientHandlerService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run() {

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
            broadcastMessage("INFOS: " + userUsername + " à rejoint le chat");

        }catch (IOException err){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void insertClientsInfosInTable(String userUsername, String userPassword) {
        try {
            this.userService.saveUsers(userUsername, userPassword);
        }catch(HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    public void broadcastMessage(String messageToSend) {
        for (ClientHandlerService clientHandler : clientHandlers) {
            try {
                if (!clientHandler.userUsername.equals(userUsername)) {
                    clientHandler.bufferedWriter.write(messageToSend);
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
