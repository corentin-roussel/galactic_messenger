package org.example;
import java.net.*;
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
                bufferedWriter.write(username + ": " + messageToSend);
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





    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bonjour, veuillez vous connecter (1) ou vous enregistrer (2) pour accéder au chat");
        int choice = Integer.parseInt(scanner.nextLine());

        if (choice == 1) {
            String[] userInfo = getUserInfo(scanner);
            if (userInfo != null) {
                Client client = connectToServer(userInfo);
                if (client != null) {
                    startThreads(client);
                }
            }
        } else if (choice == 2) {
            String[] userInfo = getUserInfo(scanner);
            if (userInfo != null) {
                System.out.println("Enregistrement réussi !");
                Client client = connectToServer(userInfo);
                if (client != null) {
                    startThreads(client);
                }
            }
        }
    }

    public static String[] getUserInfo(Scanner scanner) {
        System.out.println("Entrez votre pseudo : ");
        String username = scanner.nextLine();
        System.out.println("Entrez votre mot de passe : ");
        String password = scanner.nextLine();
        return new String[]{username, password};
    }

    public static Client connectToServer(String[] userInfo) {
        try {
            Socket socket = new Socket("localhost", 6000);
            Client client = new Client(socket, userInfo[0], userInfo[1]);
            client.bufferedWriter.write(userInfo[0]);
            client.bufferedWriter.newLine();
            client.bufferedWriter.write(userInfo[1]);
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



}



