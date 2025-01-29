package org.boilerplate.circuit.Payload;

public record ServiceHealth(
    String serviceName,
    String status,
    String details
) {
    
}
