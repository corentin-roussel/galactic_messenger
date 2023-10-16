package org.example;
import java.net.*;
import java.util.Hashtable;
import java.io.*;

public class Client {
    public static void main(String[] args) {
        try {
            Socket s = new Socket("localhost", 6500);
            InputStream in = s.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(in);
            final Hashtable<String, String> message =  (Hashtable<String, String>) ois.readObject();
            System.out.println(message.get("test"));
        } catch (IOException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
