package org.NilsCorentin.server;

import org.NilsCorentin.config.Config;
import java.io.*;
import java.net.Socket;
import org.NilsCorentin.server.DbHandler;
import org.sqlite.util.StringUtils;

import java.util.*;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>(); // Garde une trace de tout les clients connectés pour que tout le monde est accées aux messages

    public static ArrayList<ClientHandler> privateChatters = new ArrayList<>();

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private boolean isLoggedIn;
    private boolean isInPrivateChat = false;
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
            this.isLoggedIn = true;
            clientHandlers.add(this);
            isInPrivateChat = false;
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
                if(messageFromClient.contains("/")) {
                    useCommand(messageFromClient);
                }else {
                    if (isInPrivateChat) {
                        broadcastPrivateMessage(color.PURPLE+ "Private : " + messageFromClient);
                    } else {
                        broadcastMessage(color.BLUE + clientUsername + ": " + messageFromClient + color.RESET);
                    }
                }


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






    public void useCommand(String messageFromClient) throws IOException {

        String[] action = messageFromClient.split(" ");
        try{
            switch(action[1]) {
                case "/private_chat":
                    sendRequestChat(messageFromClient);
                break;
                case "/accept":
                case "/decline":
                    answerRequestChat(messageFromClient);
                break;
                case "/help":
                    broadcastSelfMessage("List of available commands : /private_chat 'username' -> To send a private chat demand\n /accept 'username' -> Accept private chat\n /decline 'username' -> Decline private chat\n /exit -> Quitter \n");
                break;
                case "/exit":
                    removeClientHandler();
                    closeEverything(socket, bufferedReader, bufferedWriter);
                break;
                default:
                    broadcastMessage(messageFromClient);
            }
        }catch(IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

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

    public void broadcastPrivateMessage(String messageTosend){
        for (ClientHandler participant : privateChatters) {
            try {
                participant.bufferedWriter.write(messageTosend);
                participant.bufferedWriter.newLine();
                participant.bufferedWriter.flush();
            } catch (IOException err) {
            }
        }
    }

    public void removeClientHandler(){
        this.isLoggedIn = false;
        clientHandlers.remove(this);
        String clientLeftMsg = "INFOS: " + clientUsername + " à quitté le chat";
        String colorizedClientLeftMsg = color.colorizeText(clientLeftMsg, Config.PURPLE);
        List<String> connectedClients = ClientHandler.getConnectedClients();
        broadcastMessage(colorizedClientLeftMsg + " | " + connectedClients);


    }




    public void acceptPrivateChat(String username) {
        for(ClientHandler client: clientHandlers) {
            try {
                if (username.equals(client.clientUsername) && client.isLoggedIn) {
                    client.bufferedWriter.write(clientUsername +" accepted you'r e private chat");
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();

                    privateChatters.add(this);
                    privateChatters.add(client);

                    this.isInPrivateChat = true;
                    client.isInPrivateChat = true;
                }

            }catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void declinePrivateChat(String username) {
        for(ClientHandler client: clientHandlers) {
            try {
                if (username.equals(client.clientUsername) && client.isLoggedIn) {
                    client.bufferedWriter.write(clientUsername +" declined you're private chat");
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                }
            }catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void sendRequestChat(String messageChat) throws IOException {

        String[] result = messageChat.split(" ");
        String username = result[2].trim();

        String choiceReceiver = "/accept \"" + clientUsername  + "\"" + " or " + "/decline \"" + clientUsername + "\"";
        for(ClientHandler client: clientHandlers) {
            try {
                if (username.equals(client.clientUsername) && client.isLoggedIn) {
                    client.bufferedWriter.write(choiceReceiver);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                }
            }catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void answerRequestChat(String messageChat) throws IOException {
        String[] result = messageChat.split(" ");
        String username = result[2];

        switch(result[1]) {
            case "/accept":
                acceptPrivateChat(username);
            break;
            case "/decline":
                declinePrivateChat(username);
            break;
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





}
