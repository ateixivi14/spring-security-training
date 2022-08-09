package com.example.springsecuritytraining.controller;

import com.example.springsecuritytraining.domain.ApiUser;
import com.example.springsecuritytraining.domain.Role;
import com.example.springsecuritytraining.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping
    public ResponseEntity<List<ApiUser>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }
    
    @PostMapping
    public void saveUser(ApiUser user){
        userService.saveUser(user);
    }
    
    @PostMapping("/role")
    public void saveRole(Role role) {
        userService.saveRole(role);
    }
    
}
