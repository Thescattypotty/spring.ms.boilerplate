package org.boilerplate.gateway.Payload;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthenticationToken extends AbstractAuthenticationToken{
    private final String token;
    private String principal;

    public JwtAuthenticationToken(String token) {
        super(null);
        this.token = token;
        super.setAuthenticated(false);
    }

    public JwtAuthenticationToken(String token, Object principal, Collection<? extends GrantedAuthority> authorities){
        super(authorities);
        this.token = token;
        this.principal = (String) principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials(){
        return token;
    }

    @Override
    public String getPrincipal() {
        return principal;
    }
}
