package com.example.springsecuritytraining.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtGenerator {
    
    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private static final String KEY = "caca, super cagada y una super cagadisima";

    public String createJwtToken(User user, Date expiresAt, String issuer) {
        
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        
        Claims claims = new DefaultClaims();
        claims.put("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        
        return  Jwts.builder()
                .setExpiration(expiresAt)
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey).compact();
    }

    public Claims verifyJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(KEY))
                .parseClaimsJws(jwt).getBody();

    } 
}
