package com.houses96.apigateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.List;

public class CustomAuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private TokenProvider tokenProvider;

    public CustomAuthenticationManager() {
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = (String) authentication.getCredentials();

        // Validate the token (signature, expiration, etc.)
        if (!tokenProvider.validateToken(token)) {
            return Mono.error(new BadCredentialsException("Invalid Token"));
        }

        // Extract user details or authorities from the token
        String username = tokenProvider.getUsernameFromToken(token);
        List<GrantedAuthority> authorities = tokenProvider.getAuthoritiesFromToken(token);

        // Create an authenticated token
        CustomAuthenticationToken authenticatedToken = new CustomAuthenticationToken(username, authorities);

        return Mono.just(authenticatedToken);
    }
}
