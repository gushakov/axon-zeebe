package com.github.axonzeebe.core.command;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class CancelFlightCommand {
    @TargetAggregateIdentifier
    private final String tripId;
}
