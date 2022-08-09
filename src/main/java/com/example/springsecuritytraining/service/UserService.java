package com.example.springsecuritytraining.service;

import com.example.springsecuritytraining.domain.ApiUser;
import com.example.springsecuritytraining.domain.Role;
import com.example.springsecuritytraining.repository.RoleRepository;
import com.example.springsecuritytraining.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    private final RoleRepository roleRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    public ApiUser saveUser(ApiUser apiUser) {
        apiUser.setPassword(passwordEncoder.encode(apiUser.getPassword()));
        return userRepository.save(apiUser);
    }
    
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }
    
    public void addRoleToUser(String username, String roleName) {
        ApiUser apiUser = userRepository.findByUsername(username);
        Role role =  roleRepository.findByName(roleName);
        apiUser.getRoles().add(role);
        userRepository.save(apiUser);
    }
    
    public ApiUser getUser(String username) {
       return userRepository.findByUsername(username);
    }
    
    public List<ApiUser> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApiUser apiUser =  userRepository.findByUsername(username);
        
        if (apiUser == null) {
            throw new UsernameNotFoundException("Username not  found");
        }
        
        return new User(apiUser.getUsername(), apiUser.getPassword(), getAuthorities(apiUser.getRoles()));
    }

    private Collection<SimpleGrantedAuthority>  getAuthorities(Collection<Role> roles) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return authorities;
    }
}
