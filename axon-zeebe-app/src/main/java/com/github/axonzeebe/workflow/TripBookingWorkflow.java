package com.github.axonzeebe.workflow;

import com.github.axonzeebe.core.command.BookCarCommand;
import com.github.axonzeebe.core.command.BookHotelCommand;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.spring.client.ZeebeClientLifecycle;
import io.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Component;

import static com.github.axonzeebe.workflow.TripBookingConstants.CORRELATION_KEY;

@Component
@Slf4j
public class TripBookingWorkflow extends AbstractWorkflow {

    public TripBookingWorkflow(CommandGateway commandGateway, ZeebeClientLifecycle zeebeClient) {
        super(commandGateway, zeebeClient);
    }

    // Handle workflow jobs

    @ZeebeWorker(type = "bookCar")
    public void handleBookCarJob(final JobClient client, final ActivatedJob job) {
        handleWorkflowJob(new BookCarCommand((String) job.getVariablesAsMap().get(CORRELATION_KEY)),
                client, job);
    }

    @ZeebeWorker(type = "bookHotel")
    public void handleBookHotelJob(final JobClient client, final ActivatedJob job) {
        handleWorkflowJob(new BookHotelCommand((String) job.getVariablesAsMap().get(CORRELATION_KEY)),
                client, job);
    }
}
