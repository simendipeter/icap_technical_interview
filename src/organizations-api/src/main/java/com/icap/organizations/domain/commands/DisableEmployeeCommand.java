package com.icap.organizations.domain.commands;

import com.icap.axon.command.validation.ValidatableCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisableEmployeeCommand implements ValidatableCommand {
    @TargetAggregateIdentifier
    private UUID employeeId;

}
