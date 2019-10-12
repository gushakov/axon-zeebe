package com.github.axonzeebe.workflow;

import com.github.axonzeebe.core.command.BookCarCommand;
import com.github.axonzeebe.core.command.BookHotelCommand;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.spring.client.ZeebeClientLifecycle;
import io.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TripBookingWorkflow extends AbstractWorkflow {

    private final static String TRIP_ID = "tripId";

    public TripBookingWorkflow(CommandGateway commandGateway, ZeebeClientLifecycle zeebeClient) {
        super(commandGateway, zeebeClient);
    }

    // process domain events from the trip booking aggregate by forwarding
    // them as messages to the workflow engine
    @EventHandler
    public void on(WorkflowEvent event) {
        processWorkflowEvent(event);
    }

    // handle workflow jobs assigned by the workflow engine by sending
    // appropriate commands to the trip booking aggregate
    @ZeebeWorker(type = "bookCar")
    public void handleBookCarJob(final JobClient client, final ActivatedJob job) {
        handleWorkflowJob(new BookCarCommand((String) job.getVariablesAsMap().get(TRIP_ID)),
                client, job);

    }

    @ZeebeWorker(type = "bookHotel")
    public void handleBookHotelJob(final JobClient client, final ActivatedJob job) {
        handleWorkflowJob(new BookHotelCommand((String) job.getVariablesAsMap().get(TRIP_ID)),
                client, job);
    }

}
