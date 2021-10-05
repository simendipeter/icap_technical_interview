package com.icap.organizations.config;

import com.icap.organizations.domain.EmployeeAggregate;
import org.axonframework.common.caching.JCacheAdapter;
import org.axonframework.eventsourcing.CachingEventSourcingRepository;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.GenericAggregateFactory;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.annotation.ParameterResolverFactory;
import org.axonframework.modelling.command.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmployeeRepositoryConfig {

    private static final int SNAPSHOT_EVENT_COUNT_THRESHOLD = 30;

    private final ParameterResolverFactory parameterResolverFactory;

    @Autowired
    public EmployeeRepositoryConfig(ParameterResolverFactory parameterResolverFactory) {
        this.parameterResolverFactory = parameterResolverFactory;
    }

    @Bean
    public Repository<EmployeeAggregate> repositoryForEmployee(EventStore eventStore,
                                                                   Snapshotter snapshotter,
                                                                   JCacheAdapter cacheAdapter) {

        return CachingEventSourcingRepository.builder(EmployeeAggregate.class)
                .aggregateFactory(new GenericAggregateFactory<>(EmployeeAggregate.class))
                .parameterResolverFactory(parameterResolverFactory)
                .snapshotTriggerDefinition(new EventCountSnapshotTriggerDefinition(snapshotter, SNAPSHOT_EVENT_COUNT_THRESHOLD))
                .eventStore(eventStore)
                .cache(cacheAdapter)
                .build();
    }
}
