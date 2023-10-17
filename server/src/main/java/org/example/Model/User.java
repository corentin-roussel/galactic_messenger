package org.example.Server.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "username",nullable = false, length = 100)
    private String username;

    @Column(name="password", nullable = false)
    private String password;


    protected User() {};


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String toString() {
        return String.format(
                "Customer[id=%d, firstName='%s']",
                id, username);
    }


    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
