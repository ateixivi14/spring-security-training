package com.example.springsecuritytraining.configuration;

import com.example.springsecuritytraining.jwt.JwtGenerator;
import com.example.springsecuritytraining.jwt.JwtProperties;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
@RequiredArgsConstructor
public class GenericConfiguration {

    private final JwtProperties jwtProperties;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWSSigner jwsSigner() throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        var keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(jwtProperties.getPrivateKey()));
        var privateKey = keyFactory.generatePrivate(keySpec);
        return new RSASSASigner(privateKey);
    }

    @Bean
    public JWSVerifier jwsVerifier() throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        var keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(jwtProperties.getPublicKey()));
        var publicKey = keyFactory.generatePublic(keySpec);
        return new RSASSAVerifier((RSAPublicKey) publicKey);
    }

}
