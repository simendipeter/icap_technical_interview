package com.icap.api.rest.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {

    private UUID id;
    private UUID userId;
    private String firstName;
    private String lastName;
    private Instant dateOfBirth;
    private String jobTitle;
    private String employeeNumber;
    private UUID immediateSupervisorId;
    private Instant registeredOn;
    private boolean disabled;
}
