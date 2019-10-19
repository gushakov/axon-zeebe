package com.github.axonzeebe.core.event;

import lombok.Value;

@Value
public class FlightCancelledEvent {
    private final String tripId;
}
