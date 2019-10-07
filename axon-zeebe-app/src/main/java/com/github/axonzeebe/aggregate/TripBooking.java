package com.github.axonzeebe.aggregate;

import com.github.axonzeebe.core.command.StartTripBookingCommand;
import com.github.axonzeebe.core.event.TripBookingStartedEvent;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
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
    public TripBooking(StartTripBookingCommand command){
        log.debug("[Aggregate] Process new trip command: {}", command);
        AggregateLifecycle.apply(new TripBookingStartedEvent(command.getTripId()));
    }
}
