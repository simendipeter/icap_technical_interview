package com.icap.organizations.eventhandlers;

import com.icap.axon.replay.ReplayCompletionAware;
import com.icap.organizations.domain.events.EmployeeCreatedEvent;
import com.icap.organizations.domain.events.EmployeeDisabledEvent;
import com.icap.organizations.domain.events.EmployeeEnabledEvent;
import com.icap.organizations.domain.events.EmployeeUpdateEvent;
import com.icap.organizations.persistence.EmployeeRepository;
import lombok.extern.log4j.Log4j2;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.eventhandling.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@Log4j2
public class EmployeeEventHandler implements ReplayCompletionAware {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeEventHandler(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @ResetHandler
    public void prepareForReplay() {
        LOGGER.info("{} deleting projections", EmployeeEventHandler.class.getSimpleName());
        employeeRepository.deleteAll();
    }

    @EventHandler
    void on(EmployeeCreatedEvent event, @Timestamp Instant creationTime) {
        LOGGER.info("Creating new employee {}", event.getEmployeeId());
        employeeRepository.createEmployee(event.getEmployeeId(), event.getUserId(),
                event.getFirstName(), event.getLastName(), event.getDateOfBirth(), event.getJobTitle(),
                event.getEmployeeNumber(), event.getImmediateSupervisorId(), creationTime, event.isDisabled());
    }

    @EventHandler
    void on(EmployeeDisabledEvent event) {
        LOGGER.info("Employee {}  is disabled", event.getEmployeeId());
        var persistableEmployee = employeeRepository.findById(event.getEmployeeId()).orElseThrow();
        persistableEmployee.setDisabled(true);
        employeeRepository.save(persistableEmployee);
    }

    @EventHandler
    void on(EmployeeEnabledEvent event) {
        LOGGER.info("Employee {} is enabled", event.getEmployeeId());
        var persistableEmployee = employeeRepository.findById(event.getEmployeeId()).orElseThrow();
        persistableEmployee.setDisabled(false);
        employeeRepository.save(persistableEmployee);
    }

    @EventHandler
    void on(EmployeeUpdateEvent event) {
        LOGGER.info("Employee {} is updated", event.getEmployeeId());
        var employee = employeeRepository.findById(event.getEmployeeId()).orElseThrow();
        employee.setFirstName(selectDesiredState(event.getFirstName(), employee.getFirstName()));
        employee.setLastName(selectDesiredState(event.getLastName(), employee.getLastName()));
        employee.setDateOfBirth(selectDesiredState(event.getDateOfBirth(), employee.getDateOfBirth()));
        employee.setImmediateSupervisorId(selectDesiredState(event.getImmediateSupervisorId(), employee.getImmediateSupervisorId()));
        employee.setJobTitle(selectDesiredState(event.getJobTitle(), employee.getJobTitle()));

        employeeRepository.save(employee);
    }


    private String selectDesiredState(String desiredState, String currentState) {
        return desiredState == null ? currentState : desiredState;
    }

    private UUID selectDesiredState(UUID desiredState, UUID currentState) {
        return desiredState == null ? currentState : desiredState;
    }

    private Instant selectDesiredState(Instant desiredState, Instant currentState) {
        return desiredState == null ? currentState : desiredState;
    }
}
