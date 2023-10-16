package org.example;
import java.net.*;
import java.util.Hashtable;
import java.io.*;

public class Server {
    public static void main(String[] args){

        try {
            ServerSocket ss =  new ServerSocket(6500);
            Hashtable <String, String> message = new Hashtable<String, String>();
            message.put("test", "bonjour ceci est un test tes grand morts");
 // implémenter authentification ici je pense 
            Socket client = ss.accept();
            System.out.println(message.get("test")); 
           
            System.out.println("nouvelle connection");

            OutputStream out = client.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(message);
            client.close();            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }















        //CreateDbHandler db = new CreateDbHandler();
        //db.getConn();
        
    }
}