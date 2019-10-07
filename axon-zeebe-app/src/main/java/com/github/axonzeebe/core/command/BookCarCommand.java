package com.github.axonzeebe.core.command;

import lombok.Value;

@Value
public class BookCarCommand {
    private final String tripId;
}
