package com.icap.functionaltests.helpers;

import com.icap.organizations.domain.events.OrganizationRegisteredEvent;
import lombok.Getter;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ReplayStatus;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TestEventHandler {

    @Getter
    private final AtomicInteger counter = new AtomicInteger(0);

    @EventHandler
    void on(OrganizationRegisteredEvent event, ReplayStatus replayStatus) {
        if (!replayStatus.isReplay()) {
            counter.incrementAndGet();
        }
    }
}
