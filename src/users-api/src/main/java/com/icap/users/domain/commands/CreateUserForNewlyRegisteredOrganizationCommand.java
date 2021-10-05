package com.icap.users.domain.commands;

import com.icap.axon.command.validation.ValidatableCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CreateUserForNewlyRegisteredOrganizationCommand implements ValidatableCommand {
    @TargetAggregateIdentifier
    UUID userId;
    UUID organizationId;
    String userEmail;
    String encodedPassword;
    String displayName;
}
