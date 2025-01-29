package org.boilerplate.gateway.Configuration;

import org.boilerplate.gateway.Component.JwtAccessDeniedHandler;
import org.boilerplate.gateway.Component.JwtAuthenticationFilter;
import org.boilerplate.gateway.Component.JwtLogoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.AnonymousSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.FormLoginSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.HttpBasicSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class GatewaySecurityConfiguration {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtLogoutHandler jwtLogoutHandler;
    private final CorsConfiguration corsConfiguration;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        return http
            .csrf(CsrfSpec::disable)
            .cors(cors -> cors.configurationSource(corsConfiguration.corsConfigurationSource()))
            .httpBasic(HttpBasicSpec::disable)
            .formLogin(FormLoginSpec::disable)
            .anonymous(AnonymousSpec::disable)
            //here we can do alot of things depends on context !!
            .authorizeExchange(null)
            .addFilterBefore(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .logout(logout -> logout.logoutUrl("/api/v1/auth/logout")
                    .logoutHandler(jwtLogoutHandler)
                    .logoutSuccessHandler((exchange, authentication) -> {
                        exchange.getExchange().getResponse().setStatusCode(HttpStatus.OK);
                        return exchange.getExchange().getResponse().setComplete();
                    }
                )
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((exchange, e) -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                })
                .accessDeniedHandler(jwtAccessDeniedHandler)
            )
            .build();
    }
}
