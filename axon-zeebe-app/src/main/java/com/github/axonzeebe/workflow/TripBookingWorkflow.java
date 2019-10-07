package com.github.axonzeebe.workflow;

import com.github.axonzeebe.core.command.BookCarCommand;
import com.github.axonzeebe.core.event.TripBookingStartedEvent;
import io.zeebe.client.api.ZeebeFuture;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.response.WorkflowInstanceEvent;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.spring.client.ZeebeClientLifecycle;
import io.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Collections;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Slf4j
@Component
public class TripBookingWorkflow {

    private final CommandGateway commandGateway;

    private final ZeebeClientLifecycle zeebeClient;

    // from https://github.com/zeebe-io/spring-zeebe/blob/master/examples/worker/src/main/java/io/zeebe/spring/example/WorkerApplication.java
    private static void logJob(final ActivatedJob job) {
        log.info(
                "complete job\n>>> [type: {}, key: {}, element: {}, workflow instance: {}]\n{deadline; {}]\n[headers: {}]\n[variables: {}]",
                job.getType(),
                job.getKey(),
                job.getElementId(),
                job.getWorkflowInstanceKey(),
                Instant.ofEpochMilli(job.getDeadline()),
                job.getCustomHeaders(),
                job.getVariables());
    }

    @EventHandler
    public void on(TripBookingStartedEvent event){

        // start workflow instance, set tripId variable from the event
        log.debug("[Workflow] Starting workflow instance");
        zeebeClient.newCreateInstanceCommand()
                .bpmnProcessId("TripBookingProcess")
                .latestVersion()
                .variables(Collections.singletonMap("tripId", event.getTripId()))
                .send();

    }

    // process car booking job
    @ZeebeWorker(type = "bookCar")
    public void handleBookCarJob(final JobClient client, final ActivatedJob job) {
        log.debug("[Workflow] Handle bookCar job");
        logJob(job);

        Mono.fromFuture(commandGateway
                .send(new BookCarCommand((String) job.getVariablesAsMap().get("tripId"))));

        client.newCompleteCommand(job.getKey()).send();

    }

}
