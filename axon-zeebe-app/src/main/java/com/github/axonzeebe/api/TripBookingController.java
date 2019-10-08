package com.github.axonzeebe.api;

import com.github.axonzeebe.core.command.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.Future;

@RequiredArgsConstructor
@Slf4j
@RestController
public class TripBookingController {

    private final CommandGateway commandGateway;

    @PostMapping("/trips")
    public Future<String> startTripBooking(){
        log.debug("[API] Creating new trip booking");
        return commandGateway.send(new RequestTripBookingCommand(UUID.randomUUID().toString()));
    }

    @PostMapping("/trips/{tripId}/book-car")
    public Future<String> bookCar(@PathVariable("tripId") String tripId){
        log.debug("[API] Book a car for trip {}", tripId);
        return commandGateway.send(new RequestCarBookingCommand(tripId));
    }

    @PostMapping("/trips/{tripId}/book-hotel")
    public Future<String> bookHotel(@PathVariable("tripId") String tripId){
        log.debug("[API] Book a hotel for trip {}", tripId);
        return commandGateway.send(new RequestHotelBookingCommand(tripId));
    }

}
