package com.github.axonzeebe.core.event;

import lombok.Value;

@Value
public class TripBookingStartedEvent {
    private final String tripId;
}
