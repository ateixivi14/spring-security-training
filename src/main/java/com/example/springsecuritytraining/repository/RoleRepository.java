package com.example.springsecuritytraining.repository;

import com.example.springsecuritytraining.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    
    Role findByName(String name);
}
