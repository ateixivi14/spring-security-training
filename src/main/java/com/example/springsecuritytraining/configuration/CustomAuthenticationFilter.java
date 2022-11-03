package com.example.springsecuritytraining.configuration;

import com.example.springsecuritytraining.jwt.JwtGenerator;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        //Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) authResult.getAuthorities();
        Date expiresAt =  new Date(System.currentTimeMillis()+ 10*60*1000);
        String accessToken = null;
        try {
            accessToken = jwtGenerator.createJwtToken(user, expiresAt, request.getRequestURL().toString());
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        // String accessToken = jwtGenerator.buildToken();
        response.setHeader("access_token", accessToken);
    }
    
}
