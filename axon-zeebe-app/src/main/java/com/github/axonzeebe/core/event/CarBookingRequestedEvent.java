package com.github.axonzeebe.core.event;

import lombok.Value;

@Value
public class CarBookingRequestedEvent {
    private final String tripId;
}
