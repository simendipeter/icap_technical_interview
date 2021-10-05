package com.icap.axon.common.services;

import com.icap.axon.common.domain.Identifiable;

public interface ReadServiceProvider {
    ReadService<? extends Identifiable> getService(Class<?> clazz);

    ReadService<? extends Identifiable> getService(String classSimpleName);
}
