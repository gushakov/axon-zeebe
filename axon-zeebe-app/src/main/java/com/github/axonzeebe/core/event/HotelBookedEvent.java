package com.github.axonzeebe.core.event;

import lombok.Value;

@Value
public class HotelBookedEvent {
    private final String tripId;
}
