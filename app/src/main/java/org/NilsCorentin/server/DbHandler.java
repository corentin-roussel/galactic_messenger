package org.NilsCorentin.server;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbHandler {
    private static final String DB_URL = "jdbc:sqlite:galactic_database.db";

    private Connection conn;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC Driver not found");
        }
    }

    public DbHandler() {
        try {
            conn = DriverManager.getConnection(DB_URL);
            createTables();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTables(){
        try {
            PreparedStatement s = conn.prepareStatement("CREATE TABLE IF NOT EXISTS clients (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT NOT NULL," +
                    "password TEXT NOT NULL)");
            s.executeUpdate();
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String hashPassword(String password){
        String salt = BCrypt.gensalt();
        String hashedPswd = BCrypt.hashpw(password, salt);

        return hashedPswd;
    }

    public static boolean checkPassword(String password, String hashedPswd) {
        // Vérifier si le mot de passe correspond au hachage stocké
        return BCrypt.checkpw(password, hashedPswd);
    }





    public void insertClientsInfosinTable(String clientUsername,String clientPassword){
        try {
            String hashedPassword = hashPassword(clientPassword);
            String query = "INSERT INTO clients (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, clientUsername);
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getHashedPassword(String username){
        try {
            String query = "SELECT password FROM clients WHERE username = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                String result = resultSet.getString("password");
                return result;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public String getUserByName(String username){
        try {
            String query = "SELECT username FROM clients WHERE username = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getString("username");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
