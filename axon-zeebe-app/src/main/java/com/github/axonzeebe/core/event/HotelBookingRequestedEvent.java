package com.github.axonzeebe.core.event;

import lombok.Value;

@Value
public class HotelBookingRequestedEvent {
    private final String tripId;
}
