package com.example.springsecuritytraining.configuration;

import com.example.springsecuritytraining.jwt.JwtGenerator;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private static final String AUTHORITIES_KEY = "roles";
    private final JwtGenerator jwtGenerator;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/login")) {
            filterChain.doFilter(request, response);
        }
        else {
            String authorizationHeader =  request.getHeader(AUTHORIZATION);
            try {
                JWTClaimsSet claims  = jwtGenerator.verifyJWT(authorizationHeader);
                String username = claims.getSubject();
                String roles = String.join(",", (List<String>) claims.getClaims().get(AUTHORITIES_KEY));
                List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(roles);
                UsernamePasswordAuthenticationToken authenticationToken =  new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);

            } catch (JOSEException | ParseException e) {
                e.printStackTrace();
            }
        }

    }
}
