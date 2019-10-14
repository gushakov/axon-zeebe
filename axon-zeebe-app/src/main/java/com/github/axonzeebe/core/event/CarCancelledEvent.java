package com.github.axonzeebe.core.event;

import lombok.Value;

@Value
public class CarCancelledEvent {
    private final String tripId;
}
