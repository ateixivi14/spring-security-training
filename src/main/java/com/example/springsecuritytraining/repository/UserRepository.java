package com.example.springsecuritytraining.repository;

import com.example.springsecuritytraining.domain.ApiUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<ApiUser, Long> {
    
    ApiUser findByUsername(String username);
}
