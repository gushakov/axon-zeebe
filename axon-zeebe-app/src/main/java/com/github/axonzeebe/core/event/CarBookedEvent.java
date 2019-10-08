package com.github.axonzeebe.core.event;

import lombok.Value;

@Value
public class CarBookedEvent {

    private final String tripId;
}
