package com.icap.organizations.services;

import com.icap.axon.common.domain.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Identifiable {

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
