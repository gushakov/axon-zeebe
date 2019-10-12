package com.github.axonzeebe.workflow;

import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.spring.client.ZeebeClientLifecycle;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
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

    void processWorkflowEvent(WorkflowEvent workflowEvent) {
        // start new instance of the workflow when receiving trip booking started event from the aggregate
        if (workflowEvent.isStartingEvent()) {
            log.debug("[Workflow] Starting workflow instance, event: {}", workflowEvent);
            startWorkflowInstance(workflowEvent);
        } else {
            // else just forward this event to the workflow engine
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
        // send message to workflow engine
        zeebeClient.newPublishMessageCommand()
                .messageName(workflowEvent.getClass().getSimpleName())
                .correlationKey(workflowEvent.getCorrelationKey())
                .send();
    }

    void handWorkflowJob(Object command, final JobClient client, final ActivatedJob job) {
        log.debug("[Workflow] Handle job of type {} with key {} and variables {}",
                job.getType(), job.getKey(), job.getVariables());
        Mono.fromFuture(commandGateway
                .send(command))
                .doOnSuccess(result -> client.newCompleteCommand(job.getKey()).send()).block();
    }

}
