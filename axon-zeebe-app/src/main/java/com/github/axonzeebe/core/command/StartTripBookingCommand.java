package com.github.axonzeebe.core.command;

import lombok.Value;

@Value
public class StartTripBookingCommand {
    private final String tripId;
}
