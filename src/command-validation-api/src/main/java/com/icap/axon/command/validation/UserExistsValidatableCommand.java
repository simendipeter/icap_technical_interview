package com.icap.axon.command.validation;

import java.util.UUID;

public interface UserExistsValidatableCommand extends ValidatableCommand {

    UUID getUserIdId();
}
