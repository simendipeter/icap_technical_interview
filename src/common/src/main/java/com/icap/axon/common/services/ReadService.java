package com.icap.axon.common.services;

import com.icap.axon.common.domain.Identifiable;

import java.util.UUID;

public interface ReadService<T extends Identifiable> {
    T getById(UUID id);
}
