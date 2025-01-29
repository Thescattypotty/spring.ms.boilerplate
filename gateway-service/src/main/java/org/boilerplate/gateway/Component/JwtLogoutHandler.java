package org.boilerplate.gateway.Component;

import org.boilerplate.gateway.Service.BlackListService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements ServerLogoutHandler{

    private final BlackListService blackListService;

    @Override
    public Mono<Void> logout(
        WebFilterExchange exchange,
        Authentication authentication
        ) {
            return Mono.justOrEmpty(exchange.getExchange().getRequest().getHeaders().getFirst("Authorization"))
                .filter(authHeader -> authHeader.startsWith("Bearer "))
                .map(authHeader -> authHeader.substring(7))
                .flatMap(token -> {
                blackListService.addTokenToBlackList(token);
                return Mono.empty();
            });
        
    }
    
}
