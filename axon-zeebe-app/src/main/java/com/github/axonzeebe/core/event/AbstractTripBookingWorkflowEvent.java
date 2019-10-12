package com.github.axonzeebe.core.event;

import com.github.axonzeebe.workflow.WorkflowEvent;

public abstract class AbstractTripBookingWorkflowEvent implements WorkflowEvent {
    @Override
    public String getProcessId() {
        return "TripBookingProcess";
    }

    @Override
    public String getCorrelationKey() {
        return "tripId";
    }

    @Override
    public String getCorrelationValue() {
        return getTripId();
    }

    public abstract String getTripId();
}
