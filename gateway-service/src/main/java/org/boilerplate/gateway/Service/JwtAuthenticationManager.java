package org.boilerplate.gateway.Service;

import org.boilerplate.gateway.Exception.InvalidTokenException;
import org.boilerplate.gateway.Payload.JwtAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager{

    private final JwtService jwtService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
            .map(auth -> validateAndSetAuthentication(auth.getCredentials().toString()))
            .onErrorResume(e -> Mono.error(new InvalidTokenException("Invalid token")));
    }

    private Authentication validateAndSetAuthentication(String token) {
        if(jwtService.isTokenValid(token)) {
            return new JwtAuthenticationToken(token,null, jwtService.getClaims(token));
        }
        return null;     
    }
    
}
