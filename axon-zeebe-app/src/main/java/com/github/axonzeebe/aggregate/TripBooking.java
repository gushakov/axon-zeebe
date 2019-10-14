package com.github.axonzeebe.aggregate;

import com.github.axonzeebe.core.command.BookCarCommand;
import com.github.axonzeebe.core.command.BookHotelCommand;
import com.github.axonzeebe.core.command.CancelCarCommand;
import com.github.axonzeebe.core.command.CancelHotelCommand;
import com.github.axonzeebe.core.event.CarBookedEvent;
import com.github.axonzeebe.core.event.CarCancelledEvent;
import com.github.axonzeebe.core.event.HotelBookedEvent;
import com.github.axonzeebe.core.event.HotelCancelledEvent;
import com.github.axonzeebe.workflow.WorkflowCommand;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import javax.persistence.Entity;
import javax.persistence.Id;

@Aggregate
@Entity
@Data
@Slf4j
public class TripBooking {

    @Id
    @AggregateIdentifier
    private String id;

    private boolean carBooked;
    private boolean hotelBooked;
    private boolean flightBooked;

    public TripBooking() {
    }

    // Handle generic workflow command message
    @CommandHandler
    public TripBooking(WorkflowCommand command) {
        log.debug("[Aggregate][Workflow Command] Process workflow command: {}", command);
        AggregateLifecycle.apply(command.toWorkflowEvent());
    }

    @CommandHandler
    public void handle(BookCarCommand command) {
        log.debug("[Aggregate][Command] Confirm car booking: {}", command);
        AggregateLifecycle.apply(new CarBookedEvent(command.getTripId()));
    }

    @CommandHandler
    public void handle(BookHotelCommand command) {
        log.debug("[Aggregate][Command] Confirm hotel booking: {}", command);
        AggregateLifecycle.apply(new HotelBookedEvent(command.getTripId()));
    }

    @CommandHandler
    public void handle(CancelCarCommand command) {
        log.debug("[Aggregate][Command] Cancel car: {}", command);
        AggregateLifecycle.apply(new CarCancelledEvent(command.getTripId()));
    }

    @CommandHandler
    public void handle(CancelHotelCommand command) {
        log.debug("[Aggregate][Command] Cancel hotel: {}", command);
        AggregateLifecycle.apply(new HotelCancelledEvent(command.getTripId()));
    }

    @EventHandler
    public void on(CarBookedEvent event) {
        log.debug("[Aggregate][Event] Register car booking: {}", event);
        this.carBooked = true;
    }

    @EventHandler
    public void on(HotelBookedEvent event) {
        log.debug("[Aggregate][Event] Register hotel booking: {}", event);
        this.hotelBooked = true;
    }

    @EventHandler
    public void on(CarCancelledEvent event) {
        log.debug("[Aggregate][Event] Register car cancellation: {}", event);
        this.carBooked = false;
    }

    @EventHandler
    public void on(HotelCancelledEvent event) {
        log.debug("[Aggregate][Event] Register hotel cancellation: {}", event);
        this.hotelBooked = false;
    }

}
