package org.example.Services;

import java.net.*;
import java.util.Hashtable;
import java.io.*;
import java.net.InetAddress;
import java.util.Scanner;
import org.example.Services.ClientServices;
public class ClientServices {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private  String username;

    private  String password;





    public ClientServices(Socket socket, String username, String password){
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
                bufferedWriter.write(messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch (IOException err){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }







    public void listenForMessage(){
        new Thread(() -> {
            String messageFromGroupChat;
            while (socket.isConnected()){
                try {
                    messageFromGroupChat = bufferedReader.readLine();
                    System.out.println(messageFromGroupChat);
                }catch (IOException err){
                    closeEverything(socket, bufferedReader, bufferedWriter);
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







    public static String[] getUserInfo(Scanner scanner) {
        System.out.println("Username : ");
        String username = scanner.nextLine();
        System.out.println("Password : ");
        String password = scanner.nextLine();
        return new String[]{username, password};
    }

    public static ClientServices connectToServer(String[] userInfo) {
        try {
            Socket socket = new Socket("127.0.0.1", 3000);
            ClientServices client = new ClientServices(socket, userInfo[0], userInfo[1]);
            client.bufferedWriter.write(userInfo[0]);
            client.bufferedWriter.newLine();
            client.bufferedWriter.flush();
            return client;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void startThreads(ClientServices client) {
        Thread listenThread = new Thread(client::listenForMessage);
        listenThread.start();
        // Démarrer un thread pour envoyer des messages
        Thread sendThread = new Thread(client::sendMessage);
        sendThread.start();
    }

}
