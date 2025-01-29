package org.boilerplate.gateway.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Service
public class JwtService {
    
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String getSubject(String token) {
        return JWT.require(Algorithm.HMAC256(secret))
            .build()
            .verify(token)
            .getSubject();
    }
    public Set<GrantedAuthority> getClaims(String token) {
        return JWT.require(Algorithm.HMAC256(secret))
            .build()
            .verify(token)
            .getClaims()
            .entrySet()
            .stream()
            .map(entry -> new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return entry.getKey();
                }
            })
            .collect(HashSet::new, HashSet::add, HashSet::addAll);
    }

    public boolean isTokenExpired(String token) {
        return JWT.require(Algorithm.HMAC256(secret))
            .build()
            .verify(token)
            .getExpiresAt()
            .before(new Date());
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }
}
