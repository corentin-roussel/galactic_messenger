package org.NilsCorentin.client;

import org.NilsCorentin.server.ClientHandler;
import org.NilsCorentin.config.Config;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.io.*;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private  String username;

    private  String password;

    Config color = new Config();



    public Client(Socket socket, String username, String password){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
            this.password = password;
        }catch (IOException err){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }







    public void sendMessage(){
        try {
            bufferedWriter.write((username));
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(Config.BLUE + username + Config.RESET +": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch (IOException err){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }







    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromGroupChat;
                while (socket.isConnected()){
                    try {
                        messageFromGroupChat = bufferedReader.readLine();
                        System.out.println(messageFromGroupChat);
                    }catch (IOException err){
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }






    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
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

    public static Client connectToServer(String[] userInfo, String[] args) {
        try {
            InetSocketAddress inetAddress = new InetSocketAddress(args[0], Integer.parseInt(args[1]));
            Socket socket = new Socket();
            socket.connect(inetAddress , 5000);
            Client client = new Client(socket, userInfo[1], userInfo[2]);
            client.bufferedWriter.write(userInfo[1]);
            client.bufferedWriter.newLine();
            client.bufferedWriter.write(userInfo[2]);
            client.bufferedWriter.newLine();
            client.bufferedWriter.flush();
            return client;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void startThreads(Client client) {
        Thread listenThread = new Thread(client::listenForMessage);
        listenThread.start();

        // Démarrer un thread pour envoyer des messages
        Thread sendThread = new Thread(client::sendMessage);
        sendThread.start();
    }


    public static void oneToOne(Client client) {

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