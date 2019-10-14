package com.github.axonzeebe.workflow;

public interface WorkflowEvent {

    default boolean isWorkflowStartingEvent() {
        return false;
    }

    String getWorkflowMessageName();

    String getProcessId();

    String getCorrelationKey();

    String getCorrelationValue();

}
