package com.icap.organizations.domain.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.serialization.Revision;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Revision("0")
public class EmployeeUpdateEvent {
    private UUID employeeId;
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
