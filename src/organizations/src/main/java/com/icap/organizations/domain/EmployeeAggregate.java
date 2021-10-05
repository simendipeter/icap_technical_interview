package com.icap.organizations.domain;

import com.icap.i18n.TranslatableExceptionFactory;
import com.icap.organizations.domain.commands.CreateRegisteredEmployeeCommand;
import com.icap.organizations.domain.commands.DisableEmployeeCommand;
import com.icap.organizations.domain.commands.EnableEmployeeCommand;
import com.icap.organizations.domain.commands.UpdateEmployeeCommand;
import com.icap.organizations.domain.events.EmployeeCreatedEvent;
import com.icap.organizations.domain.events.EmployeeDisabledEvent;
import com.icap.organizations.domain.events.EmployeeEnabledEvent;
import com.icap.organizations.domain.events.EmployeeUpdateEvent;
import com.icap.organizations.domain.events.OrganizationDisabledByAdminEvent;
import com.icap.organizations.domain.events.OrganizationEnabledByAdminEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.spring.stereotype.Aggregate;

import java.io.Serializable;
import java.util.UUID;

import static com.icap.i18n.MessageKeys.EMPLOYEE_UPDATE_NO_FIELDS_CHANGED;
import static com.icap.i18n.MessageKeys.ORGANIZATION_ALREADY_ENABLED;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate(repository = "repositoryForEmployee")
public class EmployeeAggregate implements Serializable {

    @AggregateIdentifier
    private UUID employeeId;
    @AggregateMember
    private boolean disabled;

    protected EmployeeAggregate() {
    }

    @CommandHandler
    public EmployeeAggregate(CreateRegisteredEmployeeCommand command) {
        apply(new EmployeeCreatedEvent(command.getEmployeeId(), command.getUserId(), command.getFirstName(), command.getLastName(),
                command.getDateOfBirth(), command.getJobTitle(), command.getEmployeeNumber(), command.getImmediateSupervisorId(),
                command.getRegisteredOn(), command.isDisabled()));
    }

    @CommandHandler
    void handle(DisableEmployeeCommand command) {
        validateEmployeeIsEnabled();
        apply(new EmployeeDisabledEvent(command.getEmployeeId()));
    }

    @CommandHandler
    void handle(EnableEmployeeCommand command) {
        if (!disabled) {
            TranslatableExceptionFactory.throwForKey(ORGANIZATION_ALREADY_ENABLED, employeeId);
        }
        apply(new EmployeeEnabledEvent(command.getEmployeeId()));
    }

    @CommandHandler
    public void handle(UpdateEmployeeCommand command) throws Throwable {
        validateEmployeeIsEnabled();
        validateAtLeastOneUpdateIsMade(command);

        if (isEmployeeUpdated(command)) {
            apply(new EmployeeUpdateEvent(command.getEmployeeId(), command.getUserId(), command.getFirstName(), command.getLastName(),
                    command.getDateOfBirth(), command.getJobTitle(), command.getEmployeeNumber(), command.getImmediateSupervisorId(),
                    command.getRegisteredOn(), command.isDisabled()));
        }
    }

    @EventSourcingHandler
    void on(EmployeeCreatedEvent event) {
        employeeId = event.getEmployeeId();
        disabled = false;
    }

    @EventSourcingHandler
    void on(OrganizationDisabledByAdminEvent event) {
        disabled = true;
    }

    @EventSourcingHandler
    void on(OrganizationEnabledByAdminEvent event) {
        disabled = false;
    }


    private void validateEmployeeIsEnabled() {
        if (disabled) {
            TranslatableExceptionFactory.throwForKey(EMPLOYEE_UPDATE_NO_FIELDS_CHANGED, employeeId);
        }
    }

    private void validateAtLeastOneUpdateIsMade(UpdateEmployeeCommand command) throws Throwable {
        var isChangeMade = isEmployeeUpdated(command);
        if (!isChangeMade) {
            TranslatableExceptionFactory.throwForKey(EMPLOYEE_UPDATE_NO_FIELDS_CHANGED);
        }
    }

    private boolean isEmployeeUpdated(UpdateEmployeeCommand command) {
        return command.getFirstName() != null
                || command.getLastName() != null
                || command.getDateOfBirth() != null
                || command.getEmployeeNumber() != null
                || command.getImmediateSupervisorId() != null
                || command.getJobTitle() != null;
    }

}
