package org.example.Model;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;


@Entity
@Table(name = "users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username",nullable = false, length = 100)
    private String username;

    @Column(name="password", nullable = false)
    private String password;



    protected UserModel() {};


    public UserModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String toString() {
        return String.format(
                "Customer[id=%d, username='%s', password=%s]",
                id, username, password);
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
