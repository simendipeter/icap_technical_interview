package com.icap.axon.command.validation;

import java.util.UUID;

public interface OrganizationStatusValidatableCommand extends ValidatableCommand {

    UUID getOrganizationId();
}
