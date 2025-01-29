package org.boilerplate.gateway.Component;

import org.boilerplate.gateway.Payload.JwtAuthenticationToken;
import org.boilerplate.gateway.Service.BlackListService;
import org.boilerplate.gateway.Service.JwtAuthenticationManager;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {
    private final JwtAuthenticationManager jwtAuthenticationManager;
    private final BlackListService blackListService;

    @NonNull
    @Override
    public Mono<Void> filter(
        @NonNull ServerWebExchange exchange,
        @NonNull WebFilterChain chain
    ){
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("Authorization"))
            .filter(authHeader -> authHeader.startsWith("Bearer "))
            .map(authHeader -> authHeader.substring(7))
            .filter(token -> !blackListService.isTokenBlackListed(token))
            .flatMap(token -> {
                return jwtAuthenticationManager.authenticate(new JwtAuthenticationToken(token))
                    .flatMap(authentication -> {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        return chain.filter(exchange);
                    })
                    .onErrorResume(e -> {
                        SecurityContextHolder.clearContext();
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return chain.filter(exchange);
                    });
            })
            .switchIfEmpty(chain.filter(exchange));
    }
}
