package com.github.axonzeebe.core.event;

import lombok.Value;

@Value
public class TripBookingRequestedEvent{
    private final String tripId;
}
