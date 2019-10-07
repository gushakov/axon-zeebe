package com.github.axonzeebe.api;

import com.github.axonzeebe.core.command.StartTripBookingCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.Future;

@RequiredArgsConstructor
@Slf4j
@RestController
public class TripBookingController {

    private final CommandGateway commandGateway;

    @PostMapping
    public Future<String> startTripBooking(){
        log.debug("[API] Creating trip booking.");
        return commandGateway.send(new StartTripBookingCommand(UUID.randomUUID().toString()));
    }

}
