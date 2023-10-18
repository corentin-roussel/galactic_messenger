package org.example;
import java.net.*;
import java.sql.*;
import java.util.Hashtable;
import java.io.*;
import java.net.InetAddress;

public class Server {

        private ServerSocket ss;


        public Server(ServerSocket ss){

            this.ss = ss;
        }








    public void startServer(){

            try {
                InetAddress ip = InetAddress.getLocalHost();
                String hostAddress = ip.getHostAddress();
                System.out.println("Adresse ip du serveur : " + hostAddress);

                while(!ss.isClosed()){
                    Socket socket = ss.accept();
                    System.out.println("Nouvelle connection !" );
                    ClientHandler clientHandler = new ClientHandler(socket);

                    Thread thread = new Thread(clientHandler);
                    thread.start();

                }
            }catch (IOException err){
                err.printStackTrace();
            }
    }








    public void initDb(){
            try(Connection connection = DriverManager.getConnection("jdbc:sqlite:galactic_messenger.db");
                Statement statement = connection.createStatement()) {
                String createTableSQL = "CREATE TABLE IF NOT EXISTS clients (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "username TEXT NOT NULL," +
                        "password TEXT NOT NULL)";
                statement.executeUpdate(createTableSQL);
                System.out.println("table crée avec succées");

            }catch (SQLException err){
                err.printStackTrace();
            }
    }









    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(6000);
        Server server = new Server(ss);
        server.initDb();
        server.startServer();

    }














   /*public void shutServer(){
            try {
                if (ss != null){
                    ss.close();
                }
            }catch (IOException err){
                err.printStackTrace();
            }
    }*/
        

}