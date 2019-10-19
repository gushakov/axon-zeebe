package com.github.axonzeebe.core.event;

import lombok.Value;

@Value
public class FlightBookedEvent {
    private final String tripId;
}
