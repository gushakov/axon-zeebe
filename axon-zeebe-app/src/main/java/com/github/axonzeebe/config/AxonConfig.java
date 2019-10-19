package com.github.axonzeebe.config;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventhandling.gateway.DefaultEventGateway;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class AxonConfig {


    // Simple command bus using Spring's PlatformTransactionManager

    @Bean
    public CommandBus commandBus(PlatformTransactionManager platformTransactionManager) {
        return SimpleCommandBus.builder()
                .transactionManager(new SpringTransactionManager(platformTransactionManager))
                .build();
    }

    // Simple event bus

    @Bean
    public EventBus eventBus() {
        return SimpleEventBus.builder().build();
    }

    @Bean
    public EventGateway eventGateway(){
        return DefaultEventGateway.builder().eventBus(eventBus()).build();
    }

    // Do not need to declare JPA repositories, just annotating a domain class with @Aggregate
    // and @Entity will create this type of repository for the corresponding type automatically

}