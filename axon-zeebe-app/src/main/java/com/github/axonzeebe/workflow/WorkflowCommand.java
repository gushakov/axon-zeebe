package com.github.axonzeebe.workflow;

public interface WorkflowCommand {

    default boolean isWorkflowStartingCommand(){
        return false;
    }

    default WorkflowEvent toWorkflowEvent(){
       WorkflowCommand command = this;
       return new WorkflowEvent() {
            @Override
            public boolean isWorkflowStartingEvent() {
                return command.isWorkflowStartingCommand();
            }

            @Override
            public String getWorkflowMessageName() {
                return command.getWorkflowMessageName();
            }

            @Override
            public String getProcessId() {
                return command.getProcessId();
            }

            @Override
            public String getCorrelationKey() {
                return command.getCorrelationKey();
            }

            @Override
            public String getCorrelationValue() {
                return command.getCorrelationValue();
            }

        };
    }
    String getWorkflowMessageName();

    String getProcessId();

    String getCorrelationKey();

    String getCorrelationValue();

}
