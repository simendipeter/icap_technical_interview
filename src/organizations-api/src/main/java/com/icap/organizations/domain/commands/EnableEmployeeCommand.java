package com.icap.organizations.domain.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnableEmployeeCommand {
    @TargetAggregateIdentifier
    private UUID employeeId;

}
