package com.icap.organizations.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<PersistableEmployee, UUID> {

    default void createEmployee(UUID id, UUID userId, String firstName, String lastName, Instant dateOfBirth, String jobTitle,
                                String employeeNumber, UUID immediateSupervisorId, Instant registeredOn, boolean disabled) {

        save(new PersistableEmployee(id, userId, firstName, lastName, dateOfBirth, jobTitle, employeeNumber,
                immediateSupervisorId, registeredOn, disabled));
    }
}
