package com.github.axonzeebe.workflow;

public interface WorkflowEvent {

    default boolean isStartingEvent() {
        return false;
    }

    String getProcessId();

    String getCorrelationKey();

    String getCorrelationValue();

}
