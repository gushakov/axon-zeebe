package com.github.axonzeebe.api;

import com.github.axonzeebe.core.command.StartTripBookingCommand;
import com.github.axonzeebe.workflow.WorkflowEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.concurrent.Future;

@RequiredArgsConstructor
@Slf4j
@RestController
public class TripBookingController {

    // command gateway will be used to send
    // commands to TripBooking aggregate
    private final CommandGateway commandGateway;

    // event gateway will be used to communicate
    // with TripBookingWorkflow
    private final EventGateway eventGateway;

    @PostMapping("/trips")
    public Future<String> startTripBooking() {
        log.debug("[API] Creating a new trip booking");
        String tripId = UUID.randomUUID().toString();

        // publish workflow start event to event gateway,
        // then dispatch command to create new aggregate,
        // all done asynchronously

        return Mono.fromRunnable(() -> eventGateway.publish(makeWorkflowEvent("StartTripBooking",
                tripId, true)))
                .then(Mono.<String>fromFuture(commandGateway.send(new StartTripBookingCommand(tripId))))
                .toFuture();
    }

    // the rest of the API calls have a straightforward logic:
    // they just dispatch the events to the workflow handler

    @PostMapping("/trips/{tripId}/book-car")
    public Future<Void> bookCar(@PathVariable("tripId") String tripId) {
        log.debug("[API] Book a car for trip {}", tripId);
        return Mono.<Void>fromRunnable(() -> eventGateway.publish(
                makeWorkflowEvent("BookCar", tripId, false))).toFuture();
    }

    @PostMapping("/trips/{tripId}/book-hotel")
    public Future<Void> bookHotel(@PathVariable("tripId") String tripId) {
        log.debug("[API] Book a hotel for trip {}", tripId);
        return Mono.<Void>fromRunnable(() -> eventGateway.publish(
                makeWorkflowEvent("BookHotel", tripId, false))).toFuture();
    }

    @PostMapping("/trips/{tripId}/book-flight")
    public Future<Void> bookFlight(@PathVariable("tripId") String tripId) {
        log.debug("[API] Book a flight for trip {}", tripId);
        return Mono.<Void>fromRunnable(() -> eventGateway.publish(
                makeWorkflowEvent("BookFlight", tripId, false))).toFuture();
    }

    @PostMapping("/trips/{tripId}/confirm-trip")
    public Future<Void> confirmTripBooking(@PathVariable("tripId") String tripId) {
        log.debug("[API] Confirm the trip booking, trip ID: {}", tripId);
        return Mono.<Void>fromRunnable(() -> eventGateway.publish(
                makeWorkflowEvent("ConfirmTripBooking", tripId, false))).toFuture();

    }

    @PostMapping("/trips/{tripId}/cancel-car")
    public Future<Void> cancelCar(@PathVariable("tripId") String tripId) {
        log.debug("[API] Cancel the car for trip {}", tripId);
        return Mono.<Void>fromRunnable(() -> eventGateway.publish(
                makeWorkflowEvent("CancelCar", tripId, false))).toFuture();

    }

    @PostMapping("/trips/{tripId}/cancel-hotel")
    public Future<Void> cancelHotel(@PathVariable("tripId") String tripId) {
        log.debug("[API] Cancel the hotel for trip {}", tripId);
        return Mono.<Void>fromRunnable(() -> eventGateway.publish(
                makeWorkflowEvent("CancelHotel", tripId, false))).toFuture();

    }

    @PostMapping("/trips/{tripId}/cancel-flight")
    public Future<Void> cancelFlight(@PathVariable("tripId") String tripId) {
        log.debug("[API] Cancel the flight for trip {}", tripId);
        return Mono.<Void>fromRunnable(() -> eventGateway.publish(
                makeWorkflowEvent("CancelFlight", tripId, false))).toFuture();

    }

    /**
     * Constructs a generic workflow event with given message and correlation value.
     * @param messageName message name, the same as specified in the intermediate catch event in the workflow
     * @param correlationValue trip ID, correlation value used to correlate workflow messages
     * @param starting whether this event is a workflow starting event (will create new workflow instance)
     * @return a workflow event
     */
    private WorkflowEvent makeWorkflowEvent(String messageName, String correlationValue, boolean starting) {
        return new WorkflowEvent() {
            @Override
            public String getWorkflowMessageName() {
                return messageName;
            }

            @Override
            public String getCorrelationValue() {
                return correlationValue;
            }

            @Override
            public boolean isWorkflowStartingEvent() {
                return starting;
            }

            @Override
            public String toString() {
                return "messageName: " + getWorkflowMessageName() +
                        ", correlationValue: " + getCorrelationValue();
            }
        };
    }
}
