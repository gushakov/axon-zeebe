package com.github.axonzeebe.core.command;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class BookCarCommand {

    @TargetAggregateIdentifier
    private final String tripId;

}
