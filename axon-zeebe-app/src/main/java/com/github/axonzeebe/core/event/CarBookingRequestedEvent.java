package com.github.axonzeebe.core.event;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class CarBookingRequestedEvent extends AbstractTripBookingWorkflowEvent {
    private final String tripId;
}
