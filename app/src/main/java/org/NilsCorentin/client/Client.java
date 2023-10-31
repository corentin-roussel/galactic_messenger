package org.NilsCorentin.client;

import org.NilsCorentin.server.ClientHandler;
import org.NilsCorentin.config.Config;
import org.NilsCorentin.server.DbHandler;
import org.mindrot.jbcrypt.BCrypt;

import java.net.*;
import java.util.Hashtable;
import java.io.*;
import java.net.InetAddress;
import java.util.List;
import java.util.Scanner;

import static org.NilsCorentin.server.ClientHandler.getConnectedClients;

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
            bufferedWriter.newLine();
            bufferedWriter.flush();
            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(color.BLUE + username + color.RESET +": " + messageToSend);
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



    public static void displayHelp() {
        String msg = "List of available commands : ";
        String colorizedMsg = Config.colorizeText(msg, Config.BLUE);
        System.out.println(colorizedMsg);

        msg = "/login 'username' 'password' -> Connexion\n";
        String msg2 = "/register 'username' 'password' -> Inscription\n";
        String msg3 = "/exit -> Quitter \n";

        colorizedMsg = Config.colorizeText(msg, Config.WHITE);
        String colorizedMsg2 = Config.colorizeText(msg2, Config.WHITE);
        String colorizedMsg3 = Config.colorizeText(msg3, Config.WHITE);
        System.out.println(colorizedMsg + colorizedMsg2 + colorizedMsg3);
    }




    public static Client connectToServer(String[] args) throws IOException {
            DbHandler db = new DbHandler();
            Config color = new Config();

            String ipArg = args[0];
            int portArg = Integer.parseInt(args[1]);
            Socket socket = new Socket(ipArg, portArg);
            Client client = null;

            try {
                String wrongCredentials = Config.colorizeText("/help to see the list of available commands", Config.RED);
                System.out.println("Welcome on galactic Messenger" + "\n" + wrongCredentials);
                boolean authenticated = false;
                Scanner scanner = new Scanner(System.in);


                 while (!authenticated) {
                     String input = scanner.nextLine();
                     String[] userInfo = input.split(" ");
                     String command = userInfo[0];


                     switch (command) {

                         case "/who":
                             String msg = "List of connected users : ";
                             List<String> connectedClients = ClientHandler.getConnectedClients();
                             System.out.println(connectedClients);
                             break;

                         case "/help":
                             displayHelp();
                             break;

                         case "/login":
                             String username = userInfo[1];
                             String password = userInfo[2];
                             String user = db.getUserByName(username);
                             String hashedPswdfromDb = db.getHashedPassword(user);
                             System.out.println(hashedPswdfromDb + password);
                             if (hashedPswdfromDb != null && BCrypt.checkpw(password, hashedPswdfromDb)) {
                                 System.out.println(Config.colorizeText("User logged in successfully!", Config.GREEN));
                                 authenticated = true;
                                 client = new Client(socket, username, password);
                                 startThreads(client);

                             } else {
                                 System.out.println(Config.colorizeText("Unknown user or Invalid credentials", Config.RED));
                             }
                             break;

                         case "/register":
                             String[] userInfo2 = input.split(" ");
                             String username2 = userInfo2[1];
                             String password2 = userInfo2[2];
                             if (db.getUserByName(username2) != null) {
                                 System.out.println(Config.colorizeText("User already exists", Config.RED));
                             } else if (username2.length() < 3 || password2.length() < 3) {
                                 System.out.println(Config.colorizeText("Username and password must be at least 3 characters long", Config.RED));
                             } else {
                                 System.out.println(Config.colorizeText("User registered successfully!", Config.GREEN));
                                 authenticated = true;
                                 db.insertClientsInfosinTable(username2, password2);
                                 client = new Client(socket, username2, password2);
                                 startThreads(client);

                             }
                                break;

                        case "/exit":
                            System.out.println(Config.colorizeText("Goodbye !", Config.GREEN));
                            System.exit(0);
                            break;

                        default:
                            System.out.println(Config.colorizeText(wrongCredentials, Config.RED));
                            break;

                     }
                 }
            }catch (Exception e) {
                e.printStackTrace();
            }
            return client;

    }

    public static void startThreads(Client client) {
        Thread listenThread = new Thread(client::listenForMessage);
        listenThread.start();

        // Démarrer un thread pour envoyer des messages
        Thread sendThread = new Thread(client::sendMessage);
        sendThread.start();
    }






      /*public static String[] getUserInfo(Scanner scanner) {
        System.out.println(Config.BLUE + "Entrez votre pseudo : " + Config.RESET);
        String username = scanner.nextLine();
        System.out.println(Config.BLUE + "Entrez votre mot de passe : "+ Config.RESET);
        String password = scanner.nextLine();
        return new String[]{username, password};
    }*/

}