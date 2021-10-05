package com.icap.users.domain.commands;

import com.icap.axon.command.validation.ValidatableCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PromoteUserToOrganizationAdminCommand implements ValidatableCommand {
    @TargetAggregateIdentifier
    private UUID organizationId;
    private UUID promotedUserId;
}
