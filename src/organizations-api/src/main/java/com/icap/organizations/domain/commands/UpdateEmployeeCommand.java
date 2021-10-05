package com.icap.organizations.domain.commands;

import com.icap.axon.command.validation.UserExistsValidatableCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeCommand implements UserExistsValidatableCommand {

    @TargetAggregateIdentifier
    private UUID employeeId;
    private UUID userId;
    private String firstName;
    private String lastName;
    private Instant dateOfBirth;
    private String jobTitle;
    private String employeeNumber;
    @NotNull
    private UUID immediateSupervisorId;
    private Instant registeredOn;
    private boolean disabled;

    @Override
    public UUID getUserIdId() {
        return userId;
    }
}
