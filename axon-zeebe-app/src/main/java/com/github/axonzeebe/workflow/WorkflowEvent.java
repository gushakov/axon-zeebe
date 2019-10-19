package com.github.axonzeebe.workflow;

public interface WorkflowEvent {

    default String getProcessId() {
        return TripBookingConstants.PROCESS_ID;
    }

    default String getCorrelationKey() {
        return TripBookingConstants.CORRELATION_KEY;
    }

    String getWorkflowMessageName();

    String getCorrelationValue();

    boolean isWorkflowStartingEvent();
}
