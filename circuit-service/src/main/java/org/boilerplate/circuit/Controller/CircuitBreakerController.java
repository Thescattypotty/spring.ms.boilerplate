package org.boilerplate.circuit.Controller;

import java.util.List;

import org.boilerplate.circuit.Payload.ServiceHealth;
import org.boilerplate.circuit.Service.CircuitBreakerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/circuit")
@RequiredArgsConstructor
public class CircuitBreakerController {
    
    private final CircuitBreakerService circuitBreakerService;

    @RequestMapping("/health")
    public ResponseEntity<List<ServiceHealth>> health() {
        return ResponseEntity.ok(circuitBreakerService.getAllCircuitStates());
    }
}
