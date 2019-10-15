package com.github.axonzeebe.workflow;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import static com.github.axonzeebe.workflow.TripBookingConstants.CORRELATION_KEY;
import static com.github.axonzeebe.workflow.TripBookingConstants.PROCESS_ID;

@Value
public class TripBookingWorkflowCommand implements WorkflowCommand {

    @TargetAggregateIdentifier
    private final String correlationValue;

    private final String workflowMessageName;

    @Override
    public String getProcessId() {
        return PROCESS_ID;
    }

    @Override
    public String getCorrelationKey() {
        return CORRELATION_KEY;
    }

}
