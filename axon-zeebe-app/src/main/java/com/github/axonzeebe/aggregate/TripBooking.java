package com.github.axonzeebe.aggregate;

import com.github.axonzeebe.core.command.*;
import com.github.axonzeebe.core.event.*;
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

    @CommandHandler
    public TripBooking(RequestTripBookingCommand command){
        log.debug("[Aggregate] Process new trip command: {}", command);
        AggregateLifecycle.apply(new TripBookingRequestedEvent(command.getTripId()));
    }

    @CommandHandler
    public void handle(RequestCarBookingCommand command){
        log.debug("[Aggregate] Process request for car booking: {}", command);
        AggregateLifecycle.apply(new CarBookingRequestedEvent(command.getTripId()));
    }

    @CommandHandler
    public void handle(BookCarCommand command){
        log.debug("[Aggregate] Confirm car booking: {}", command);
        AggregateLifecycle.apply(new CarBookedEvent(command.getTripId()));
    }

    @CommandHandler
    public void handle(RequestHotelBookingCommand command){
        log.debug("[Aggregate] Process request for hotel booking: {}", command);
        AggregateLifecycle.apply(new HotelBookingRequestedEvent(command.getTripId()));
    }

    @CommandHandler
    public void handle(BookHotelCommand command){
        log.debug("[Aggregate] Confirm hotel booking: {}", command);
        AggregateLifecycle.apply(new HotelBookedEvent(command.getTripId()));
    }

    @EventHandler
    public void on(TripBookingRequestedEvent event){
        log.debug("[Aggregate] Process start trip event: {}", event);
        this.id = event.getTripId();
    }

    @EventHandler
    public void on(CarBookedEvent event){
        log.debug("[Aggregate] Register car booking: {}", event);
        this.carBooked = true;
    }

    @EventHandler
    public void on(HotelBookedEvent event){
        log.debug("[Aggregate] Register hotel booking: {}", event);
        this.hotelBooked = true;
    }
}
