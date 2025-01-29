package org.boilerplate.gateway.Configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoureLocatorConfiguration {
    
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
            /*    
            .route("auth-service", r -> r.path("/auth/**")
                .uri("lb://auth-service"))
            */
            .build();
    }
}
