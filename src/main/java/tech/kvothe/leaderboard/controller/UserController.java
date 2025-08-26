package tech.kvothe.leaderboard.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping()
    public ResponseEntity<Void> createUser(){
        return null;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(){
        return null;
    }

    @PostMapping("/test")
    public ResponseEntity<Void> test(){
        return null;
    }
}
