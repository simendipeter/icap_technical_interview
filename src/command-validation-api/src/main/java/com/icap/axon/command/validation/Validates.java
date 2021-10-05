package com.icap.axon.command.validation;

public interface Validates<T extends ValidatableCommand> {

    void validate(T validatable);
}
