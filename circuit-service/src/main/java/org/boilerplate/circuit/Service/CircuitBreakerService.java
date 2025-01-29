package org.boilerplate.circuit.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.boilerplate.circuit.Payload.ServiceHealth;
import org.springframework.stereotype.Service;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.Metrics;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.State;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CircuitBreakerService {
    
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public List<ServiceHealth> getAllCircuitStates() {
        return circuitBreakerRegistry.getAllCircuitBreakers().stream()
            .map(this::createHealthStatus)
            .collect(Collectors.toList());
    }

    private ServiceHealth createHealthStatus(CircuitBreaker circuitBreaker) {
        Metrics metrics = circuitBreaker.getMetrics();
        State state = circuitBreaker.getState();

        return new ServiceHealth(
                circuitBreaker.getName(),
                state.name(),
                String.format("Failure rate: %.2f%%, Calls: %d, Failed: %d",
                    metrics.getFailureRate(),
                    metrics.getNumberOfSuccessfulCalls() + metrics.getNumberOfFailedCalls(),
                    metrics.getNumberOfFailedCalls()));
    }
}
