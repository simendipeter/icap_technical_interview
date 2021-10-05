package com.icap.organizations.services;


import java.time.Instant;
import java.util.UUID;

public interface EmployeeService {

    UUID createRegisteredEmployee(UUID userId, String firstName, String lastName, Instant dateOfBirth, String jobTitle,
                                  String employeeNumber, UUID immediateSupervisorId, Instant registeredOn, boolean disabled);

    void updateEmployee(UUID employeeId, UUID userId, String firstName, String lastName, Instant dateOfBirth, String jobTitle,
                        String employeeNumber, UUID immediateSupervisorId, Instant registeredOn, boolean disabled);


    void disableEmployee(UUID employeeId);

    void enableEmployee(UUID employeeId);
}
