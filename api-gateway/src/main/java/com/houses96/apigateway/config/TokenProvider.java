package com.houses96.apigateway.config;

import org.springframework.security.core.GrantedAuthority;
import java.util.List;

public interface TokenProvider {
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
    List<GrantedAuthority> getAuthoritiesFromToken(String token);
}

