package com.github.axonzeebe.core.event;

import lombok.Value;

@Value
public class HotelCancelledEvent {
    private final String tripId;
}
