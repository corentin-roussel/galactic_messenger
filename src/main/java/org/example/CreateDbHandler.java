package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class CreateDbHandler {

    public void getConn() throws ClassNotFoundException{
        String url = "jdbc:sqlite:galactic_messenger.db";
        Connection conn = null;
        Statement stmt = null;
        String user = "root";
        String pswd = "root";
        
        

        // create connection
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url, user, pswd);
            stmt = conn.createStatement();
            String sqlCmd = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, content TEXT)";
            stmt.executeUpdate(sqlCmd);
            System.out.println("DB CREER AVEC SUCCES");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        
}
}

    
