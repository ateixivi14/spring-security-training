package com.example.springsecuritytraining;

import com.example.springsecuritytraining.domain.ApiUser;
import com.example.springsecuritytraining.domain.Role;
import com.example.springsecuritytraining.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class SpringSecurityTrainingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityTrainingApplication.class, args);
    }
    
    @Bean
    CommandLineRunner run(UserService userService) {
        return args -> {
            userService.saveRole(new Role(1L, "ADMIN"));
            userService.saveRole(new Role(2L, "USER"));
            
            userService.saveUser(new ApiUser(1L, "ateixido", "alba", "1234", new ArrayList<>()));
            userService.saveUser(new ApiUser(2L, "lpascual", "lucas", "1234", new ArrayList<>()));
            
            userService.addRoleToUser("lpascual", "USER");
            userService.addRoleToUser("ateixido", "ADMIN");
        };
    }

}
