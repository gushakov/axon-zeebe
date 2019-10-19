package com.github.axonzeebe.workflow;

import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.spring.client.ZeebeClientLifecycle;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Slf4j
abstract class AbstractWorkflow {

    private final CommandGateway commandGateway;

    private final ZeebeClientLifecycle zeebeClient;

    AbstractWorkflow(CommandGateway commandGateway, ZeebeClientLifecycle zeebeClient) {
        this.commandGateway = commandGateway;
        this.zeebeClient = zeebeClient;
    }

    // Processes generic workflow event by dispatching corresponding messages
    // to Zeebe
    @EventHandler
    void processWorkflowEvent(WorkflowEvent workflowEvent) {
        // Start new instance of the workflow if needed
        if (workflowEvent.isWorkflowStartingEvent()) {
            log.debug("[Workflow] Starting workflow instance, event: {}", workflowEvent);
            startWorkflowInstance(workflowEvent);
        } else {
            // else just forward this event to Zeebe
            log.debug("[Workflow] Processing event: {}", workflowEvent);
            sendMessageToWorkflowEngine(workflowEvent);
        }
    }

    private void startWorkflowInstance(final WorkflowEvent workflowEvent) {
        zeebeClient.newCreateInstanceCommand()
                .bpmnProcessId(workflowEvent.getProcessId())
                .latestVersion()
                .variables(Collections.singletonMap(workflowEvent.getCorrelationKey(), workflowEvent.getCorrelationValue()))
                .send();
    }

    private void sendMessageToWorkflowEngine(final WorkflowEvent workflowEvent) {
        zeebeClient.newPublishMessageCommand()
                .messageName(workflowEvent.getWorkflowMessageName())
                .correlationKey(workflowEvent.getCorrelationValue())
                .send();
    }

    // Dispatches command argument to the command gateway to perform
    // workflow jobs (in aggregates).
    protected void handleWorkflowJob(Object command, final JobClient client, final ActivatedJob job) {
        log.debug("[Workflow][Job] Handle job of type {} with key {} and variables {}",
                job.getType(), job.getKey(), job.getVariables());
        Mono.fromFuture(commandGateway
                .send(command))
                .doOnSuccess(result -> client.newCompleteCommand(job.getKey()).send()).block();
    }

}
