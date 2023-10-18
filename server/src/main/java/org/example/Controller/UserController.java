package org.example.Controller;

import jakarta.annotation.PostConstruct;
import org.example.Model.UserModel;
import org.example.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @PostConstruct
    public void displayAllUsers(){
        List<UserModel> users = this.userService.findAllUsers();

        System.out.println("Liste des utilisateurs :");
        users.forEach(user -> {
            System.out.println(user.getId() + " * " + user.getUsername() + " * " + user.getPassword());
        });
    }

}
