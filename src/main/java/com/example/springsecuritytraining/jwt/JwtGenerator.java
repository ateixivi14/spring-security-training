package com.example.springsecuritytraining.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtGenerator {

    private final JWSVerifier jwsVerifier;
    private final JWSSigner jwsSigner;

    public String createJwtToken(User user, Date expiresAt, String issuer) throws JOSEException {

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issueTime(Date.from(Instant.now()))
                .subject(user.getUsername())
                .issuer(issuer)
                .expirationTime(expiresAt)
                .claim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .build();


        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet);

        signedJWT.sign(jwsSigner);

        return signedJWT.serialize();
    }

    public JWTClaimsSet verifyJWT(String jwt) throws JOSEException, ParseException {

        var message = "Invalid Token";
        var signedJWT = SignedJWT.parse(jwt);

        if (signedJWT.verify(jwsVerifier)) {
            return signedJWT.getJWTClaimsSet();
        }
        throw new JwtException(message);

    } 
}
