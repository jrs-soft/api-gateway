package com.houses96.apigateway.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;
    private final Object principal;

    // Constructor used when creating an unauthenticated token (i.e., before validation)
    public CustomAuthenticationToken(String token) {
        super(null);
        this.token = token;
        this.principal = null;  // At this stage, the principal is unknown
        setAuthenticated(false);
    }

    // Constructor used when creating an authenticated token
    public CustomAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = null;  // Token is not needed anymore after authentication
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        // Optionally, erase the token if you want to remove it from memory after authentication
    }
}

