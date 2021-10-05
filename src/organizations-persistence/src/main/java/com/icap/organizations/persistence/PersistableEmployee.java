package com.icap.organizations.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity(name = "employee")
public class PersistableEmployee {

    @Id
    private UUID id;
    @NotNull
    private UUID userId;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private Instant dateOfBirth;
    @NotNull
    private String jobTitle;
    @NotNull
    private String employeeNumber;
    @NotNull
    private UUID immediateSupervisorId;
    private Instant registeredOn;
    private boolean disabled;

    public PersistableEmployee(UUID id, @NotNull UUID userId, @NotNull String firstName, @NotNull String lastName,
                               @NotNull Instant dateOfBirth, @NotNull String jobTitle, @NotNull String employeeNumber,
                               @NotNull UUID immediateSupervisorId, Instant registeredOn, boolean disabled) {
        this.id = id;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.jobTitle = jobTitle;
        this.employeeNumber = employeeNumber;
        this.immediateSupervisorId = immediateSupervisorId;
        this.registeredOn = registeredOn;
        this.disabled = disabled;
    }
}
