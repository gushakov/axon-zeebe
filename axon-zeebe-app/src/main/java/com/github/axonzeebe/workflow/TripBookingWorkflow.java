package com.github.axonzeebe.workflow;

import com.github.axonzeebe.core.command.BookCarCommand;
import com.github.axonzeebe.core.event.*;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.spring.client.ZeebeClientLifecycle;
import io.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;

@RequiredArgsConstructor
@Slf4j
@Component
public class TripBookingWorkflow {

    private final CommandGateway commandGateway;

    private final ZeebeClientLifecycle zeebeClient;

    // start new instance of the workflow when receiving trip booking started event from the aggregate
    @EventHandler
    public void on(TripBookingRequestedEvent event) {

        // start workflow instance, set tripId variable from the event
        log.debug("[Workflow] Starting workflow instance");
        zeebeClient.newCreateInstanceCommand()
                .bpmnProcessId("TripBookingProcess")
                .latestVersion()
                .variables(Collections.singletonMap("tripId", event.getTripId()))
                .send();

    }

    @EventHandler
    public void on(CarBookingRequestedEvent event) {
        log.debug("[Workflow] Processing: {}", event);

        // send message to workflow engine
        zeebeClient.newPublishMessageCommand()
                .messageName("CarBookingRequestedEvent")
                .correlationKey(event.getTripId())
                .send();
    }

    @EventHandler
    public void on(HotelBookingRequestedEvent event) {
        log.debug("[Workflow] Processing: {}", event);

        // send message to workflow engine
        zeebeClient.newPublishMessageCommand()
                .messageName("HotelBookingRequestedEvent")
                .correlationKey(event.getTripId())
                .send();
    }

    // process car booking job
    @ZeebeWorker(type = "bookCar")
    public void handleBookCarJob(final JobClient client, final ActivatedJob job) {
        log.debug("[Workflow] Handle job of type {} with key {} and variables {}",
                job.getType(), job.getKey(), job.getVariables());

        // send car booking command to the aggregate
        Mono.fromFuture(commandGateway
                .send(new BookCarCommand((String) job.getVariablesAsMap().get("tripId"))))
                .doOnSuccess(o -> {
                    // proceed with the workflow if the command was successfully processed
                    // by the aggregate
                    log.debug("[Workflow] Successfully processed car booking, proceeding with the workflow, command returned {}", o);
                    client.newCompleteCommand(job.getKey()).send();
                }).block();

    }

    // process car booking job
    @ZeebeWorker(type = "bookHotel")
    public void handleBookHotelJob(final JobClient client, final ActivatedJob job) {
        log.debug("[Workflow] Handle job of type {} with key {} and variables {}",
                job.getType(), job.getKey(), job.getVariables());

        // send car booking command to the aggregate
        Mono.fromFuture(commandGateway
                .send(new BookCarCommand((String) job.getVariablesAsMap().get("tripId"))))
                .doOnSuccess(o -> {
                    // proceed with the workflow if the command was successfully processed
                    // by the aggregate
                    log.debug("[Workflow] Successfully processed hotel booking, proceeding with the workflow, command returned {}", o);
                    client.newCompleteCommand(job.getKey()).send();
                }).block();

    }

}
