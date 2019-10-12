package com.github.axonzeebe.core.event;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class TripBookingRequestedEvent extends AbstractTripBookingWorkflowEvent {
    private final String tripId;

    @Override
    public boolean isStartingEvent() {
        return true;
    }
}
