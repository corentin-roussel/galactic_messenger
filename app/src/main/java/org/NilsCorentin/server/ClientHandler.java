package org.NilsCorentin.server;

import org.NilsCorentin.client.Client;
import org.NilsCorentin.config.Config;
import java.io.*;
import java.net.Socket;
import org.NilsCorentin.server.DbHandler;
import org.sqlite.util.StringUtils;

import java.util.*;
import java.util.function.BiConsumer;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>(); // Garde une trace de tout les clients connectés pour que tout le monde est accées aux messages
    public static Map<String, PrivateChat> privateChats = new HashMap<>();
    private Socket socket;
    public BufferedReader bufferedReader;
    public BufferedWriter bufferedWriter;
    private boolean isLoggedIn;
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
                    broadcastMessage(messageFromClient);
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
                    closeEverything(this.socket, this.bufferedReader, this.bufferedWriter);
                break;
                default:
                    broadcastMessage(messageFromClient);
            }
        }catch(IOException e) {
            closeEverything(this.socket, this.bufferedReader, this.bufferedWriter);
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

    public void broadcastPrivateChat(String messageToSend) {

        for(Map.Entry<String, PrivateChat> set : privateChats.entrySet()) {
            
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
        this.isLoggedIn = false;
        clientHandlers.remove(this);
        String clientLeftMsg = "INFOS: " + clientUsername + " à quitté le chat";
        String colorizedClientLeftMsg = color.colorizeText(clientLeftMsg, Config.PURPLE);
        List<String> connectedClients = ClientHandler.getConnectedClients();
        broadcastMessage(colorizedClientLeftMsg + " | " + connectedClients);
    }




    public void acceptPrivateChat(String receiver, String sender) throws IOException {

        ClientHandler participant1 = null;
        ClientHandler participant2 = null;

        for(ClientHandler client: clientHandlers) {
            try {
                if (sender.equals(client.clientUsername)) {
                    client.bufferedWriter.write(clientUsername +" you've entered private_chat");
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                    participant1 = client;

                }
                if (receiver.equals(client.clientUsername) && client.isLoggedIn) {
                    client.bufferedWriter.write(clientUsername +" accepted you're private chat");
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                    participant2 = client;

                }

                String generateChatKey = generateChatKey(sender, receiver);

                PrivateChat privateChat = new PrivateChat(participant1, participant2, new ArrayList<>());

                privateChats.put(generateChatKey, privateChat);

                clientHandlers.remove(participant1);
                clientHandlers.remove(participant2);
            }catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public String generateChatKey(String senderName , String receiverName) {
        return senderName + "-" + receiverName;
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

        String choiceReceiver = " /accept \"" + clientUsername  + "\"" + " or " + "/decline \"" + clientUsername + "\"";
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
        String[] messageSplit = messageChat.split(" ");
        String receiver = messageSplit[2];
        String sender = messageSplit[0];
        sender = sender.substring(0, sender.length() - 1).trim();

        switch(messageSplit[1]) {
            case "/accept":
                acceptPrivateChat(receiver, sender);
            break;
            case "/decline":
                declinePrivateChat(receiver);
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


    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException ex) {}
    }



}
