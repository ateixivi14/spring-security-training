package com.example.springsecuritytraining.jwt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class JwtProperties {

    @Value("${jwt.public-key}")
    private String publicKey;

    @Value("${jwt.private-key}")
    private String privateKey;

}
