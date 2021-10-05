package com.icap.organizations.services;


import com.icap.axon.common.RandomFieldsGenerator;
import com.icap.organizations.domain.commands.CreateRegisteredEmployeeCommand;
import com.icap.organizations.domain.commands.DisableEmployeeCommand;
import com.icap.organizations.domain.commands.EnableEmployeeCommand;
import com.icap.organizations.domain.commands.UpdateEmployeeCommand;
import engineering.everest.axon.HazelcastCommandGateway;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class DefaultEmployeeService implements EmployeeService {

    private final RandomFieldsGenerator randomFieldsGenerator;
    private final HazelcastCommandGateway commandGateway;

    public DefaultEmployeeService(RandomFieldsGenerator randomFieldsGenerator, HazelcastCommandGateway commandGateway) {
        this.randomFieldsGenerator = randomFieldsGenerator;
        this.commandGateway = commandGateway;
    }


    @Override
    public UUID createRegisteredEmployee(UUID userId, String firstName, String lastName, Instant dateOfBirth, String jobTitle,
                                         String employeeNumber, UUID immediateSupervisorId, Instant registeredOn, boolean disabled) {
        UUID employeeId = randomFieldsGenerator.genRandomUUID();
        return commandGateway.sendAndWait(new CreateRegisteredEmployeeCommand(employeeId, userId, firstName, lastName, dateOfBirth,
                jobTitle, employeeNumber, immediateSupervisorId, registeredOn, disabled));
    }

    @Override
    public void updateEmployee(UUID employeeId, UUID userId, String firstName, String lastName, Instant dateOfBirth, String jobTitle,
                               String employeeNumber, UUID immediateSupervisorId, Instant registeredOn, boolean disabled) {
        commandGateway.sendAndWait(new UpdateEmployeeCommand(employeeId, userId, firstName, lastName, dateOfBirth, jobTitle, employeeNumber,
                immediateSupervisorId, registeredOn, disabled));

    }

    @Override
    public void disableEmployee(UUID employeeId) {
        commandGateway.sendAndWait(new DisableEmployeeCommand(employeeId));
    }

    @Override
    public void enableEmployee(UUID employeeId) {
        commandGateway.sendAndWait(new EnableEmployeeCommand(employeeId));
    }
}
