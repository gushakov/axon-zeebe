package com.github.axonzeebe.core.command;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class RequestTripBookingCommand {
    @TargetAggregateIdentifier
    private final String tripId;
}
