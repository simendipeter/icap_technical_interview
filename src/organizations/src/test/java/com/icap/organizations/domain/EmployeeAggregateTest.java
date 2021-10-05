package com.icap.organizations.domain;

import com.icap.axon.command.validators.UsersBelongToOrganizationValidator;
import com.icap.i18n.exceptions.TranslatableIllegalArgumentException;
import com.icap.i18n.exceptions.TranslatableIllegalStateException;
import com.icap.organizations.domain.commands.CreateRegisteredEmployeeCommand;
import com.icap.organizations.domain.commands.DisableEmployeeCommand;
import com.icap.organizations.domain.commands.EnableEmployeeCommand;
import com.icap.organizations.domain.commands.UpdateEmployeeCommand;
import com.icap.organizations.domain.events.EmployeeCreatedEvent;
import com.icap.organizations.domain.events.EmployeeDisabledEvent;
import com.icap.organizations.domain.events.EmployeeEnabledEvent;
import com.icap.organizations.domain.events.EmployeeUpdateEvent;
import org.axonframework.spring.stereotype.Aggregate;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.UUID;

import static com.icap.axon.AxonTestUtils.mockCommandValidatingMessageHandlerInterceptor;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class EmployeeAggregateTest {

    private static final UUID EMPLOYEE_ID = randomUUID();
    private static final UUID EMPLOYEE_USER_ID= randomUUID();
    private static final String EMPLOYEE_FIRST_NAME = "Peter";
    private static final String EMPLOYEE_LAST_NAME = "Simendi";;
    private static final Instant EMPLOYEE_DATE_OF_BIRTH = Instant.ofEpochSecond(100L);
    private static final String EMPLOYEE_JOB_TITLE = "Software Developer";
    private static final String EMPLOYEE_NUMBER = "12345";
    private static final UUID EMPLOYEE_SUPERVISOR_ID = randomUUID();
    private static final Instant EMPLOYEE_CREATED_ON = Instant.ofEpochSecond(100L);
    private static final String NO_CHANGE = null;
    private static final String MISSING_ARGUMENT = null;

    private static final UUID UUID_NO_CHANGE = null;
    private static final UUID UUID_MISSING_ARGUMENT = null;
    private static final Instant DATE_NO_CHANGE = null;

    private static final EmployeeCreatedEvent EMPLOYEE_CREATED_EVENT =
            new EmployeeCreatedEvent(EMPLOYEE_ID, EMPLOYEE_USER_ID, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME,
                    EMPLOYEE_DATE_OF_BIRTH, EMPLOYEE_JOB_TITLE, EMPLOYEE_NUMBER, EMPLOYEE_SUPERVISOR_ID, EMPLOYEE_CREATED_ON, false);

    private static final EmployeeDisabledEvent EMPLOYEE_DISABLED_EVENT = new EmployeeDisabledEvent(EMPLOYEE_ID);

    private FixtureConfiguration<EmployeeAggregate> testFixture;

    @Mock
    private UsersBelongToOrganizationValidator usersBelongToOrganizationValidator;

    @BeforeEach
    void setUp() {
        testFixture = new AggregateTestFixture<>(EmployeeAggregate.class)
                .registerCommandHandlerInterceptor(mockCommandValidatingMessageHandlerInterceptor(
                        usersBelongToOrganizationValidator));
    }

    @Test
    void aggregateHasExplicitlyDefinedRepository() {
        var employeeClass = EmployeeAggregate.class;
        var aggregateAnnotation = employeeClass.getAnnotation(Aggregate.class);
        assertEquals(aggregateAnnotation.repository(), "repositoryForEmployee");
    }

    @Test
    void emits_WhenCreateRegisteredEmployeeCommandIsValid() {
        testFixture.givenNoPriorActivity()
                .when(new CreateRegisteredEmployeeCommand(EMPLOYEE_ID, EMPLOYEE_USER_ID, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME,
                        EMPLOYEE_DATE_OF_BIRTH, EMPLOYEE_JOB_TITLE, EMPLOYEE_NUMBER, EMPLOYEE_SUPERVISOR_ID, EMPLOYEE_CREATED_ON, false))
                .expectEvents(EMPLOYEE_CREATED_EVENT);
    }

    @Test
    void employeeEnabled() {
        testFixture.given(EMPLOYEE_CREATED_EVENT)
                .when(new EnableEmployeeCommand(EMPLOYEE_ID))
                .expectNoEvents()
                .expectException(TranslatableIllegalStateException.class)
                .expectExceptionMessage("EMPLOYEE_ALREADY_ENABLED");
    }

    @Test
    void rejectsCreateRegisteredEmployeeCommand_WhenUserIdIsNull() {
        var command = new CreateRegisteredEmployeeCommand(EMPLOYEE_ID, null, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME,
                EMPLOYEE_DATE_OF_BIRTH, EMPLOYEE_JOB_TITLE, EMPLOYEE_NUMBER, EMPLOYEE_SUPERVISOR_ID,
                EMPLOYEE_CREATED_ON, false);

        testFixture.givenNoPriorActivity()
                .when(command)
                .expectNoEvents()
                .expectException(ConstraintViolationException.class);
    }

    @Test
    void rejectsCreateRegisteredEmployeeCommand_WhenSupervisorIdIsNull() {
        var command = new CreateRegisteredEmployeeCommand(EMPLOYEE_ID, EMPLOYEE_USER_ID, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME,
                EMPLOYEE_DATE_OF_BIRTH, EMPLOYEE_JOB_TITLE, EMPLOYEE_NUMBER, null,
                EMPLOYEE_CREATED_ON, false);

        testFixture.givenNoPriorActivity()
                .when(command)
                .expectNoEvents()
                .expectException(ConstraintViolationException.class);
    }

    @Test
    void rejectsCreateRegisteredOrganizationCommand_WhenFirstNameIsBlank() {
        testFixture.givenNoPriorActivity()
                .when(new CreateRegisteredEmployeeCommand(EMPLOYEE_ID, EMPLOYEE_USER_ID, NO_CHANGE, EMPLOYEE_LAST_NAME,
                        EMPLOYEE_DATE_OF_BIRTH, EMPLOYEE_JOB_TITLE, EMPLOYEE_NUMBER, null,
                        EMPLOYEE_CREATED_ON, false))
                .expectException(ConstraintViolationException.class)
                .expectNoEvents();
    }

    @Test
    void rejectsCreateRegisteredOrganizationCommand_WhenFirstLastNameIsBlank() {
        testFixture.givenNoPriorActivity()
                .when(new CreateRegisteredEmployeeCommand(EMPLOYEE_ID, EMPLOYEE_USER_ID, EMPLOYEE_FIRST_NAME, NO_CHANGE,
                        EMPLOYEE_DATE_OF_BIRTH, EMPLOYEE_JOB_TITLE, EMPLOYEE_NUMBER, EMPLOYEE_SUPERVISOR_ID,
                        EMPLOYEE_CREATED_ON, false))
                .expectException(ConstraintViolationException.class)
                .expectNoEvents();
    }

    @Test
    void rejectsCreateRegisteredOrganizationCommand_WhenFirstNameIsNull() {
        testFixture.givenNoPriorActivity()
                .when(new CreateRegisteredEmployeeCommand(EMPLOYEE_ID, EMPLOYEE_USER_ID, MISSING_ARGUMENT, NO_CHANGE,
                        EMPLOYEE_DATE_OF_BIRTH, EMPLOYEE_JOB_TITLE, EMPLOYEE_NUMBER, EMPLOYEE_SUPERVISOR_ID,
                        EMPLOYEE_CREATED_ON, false))
                .expectException(ConstraintViolationException.class)
                .expectNoEvents();
    }

    @Test
    void emits_WhenDisableEmployeeommandIsValid() {
        testFixture.given(EMPLOYEE_CREATED_EVENT)
                .when(new DisableEmployeeCommand(EMPLOYEE_ID))
                .expectEvents(new EmployeeDisabledEvent(EMPLOYEE_ID));
    }

    @Test
    void emits_WhenEnableEmployeeCommandIsValid() {
        testFixture.given(EMPLOYEE_CREATED_EVENT,
                new EmployeeDisabledEvent(EMPLOYEE_ID))
                .when(new EnableEmployeeCommand(EMPLOYEE_ID))
                .expectEvents(new EmployeeEnabledEvent(EMPLOYEE_ID));
    }

    @Test
    void rejectsEnableEmployeeCommand_WhenEmployeeIsAlreadyEnabled() {
        testFixture.given(EMPLOYEE_CREATED_EVENT)
                .when(new EnableEmployeeCommand(EMPLOYEE_ID))
                .expectNoEvents()
                .expectException(TranslatableIllegalStateException.class)
                .expectExceptionMessage("EMPLOYEE_ALREADY_ENABLED");
    }

    @Test
    void rejectsDisableEmployeeCommand_WhenEmployeeIsAlreadyDisabled() {
        testFixture.given(EMPLOYEE_CREATED_EVENT, EMPLOYEE_DISABLED_EVENT)
                .when(new DisableEmployeeCommand(EMPLOYEE_ID))
                .expectNoEvents()
                .expectException(TranslatableIllegalStateException.class)
                .expectExceptionMessage("EMPLOYEE_IS_DISABLED");
    }

    @Test
    void updateOrganizationCommandEmitsSingleEvent_WhenEmployeeDetailsChanged() {
        var command = new UpdateEmployeeCommand(EMPLOYEE_ID, UUID_NO_CHANGE, NO_CHANGE, NO_CHANGE,
                EMPLOYEE_DATE_OF_BIRTH, NO_CHANGE, NO_CHANGE, UUID_NO_CHANGE,
                EMPLOYEE_CREATED_ON, true);

        testFixture.given(EMPLOYEE_CREATED_EVENT)
                .when(command)
                .expectEvents(
                        new EmployeeUpdateEvent(EMPLOYEE_ID, EMPLOYEE_USER_ID, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME,
                                EMPLOYEE_DATE_OF_BIRTH, EMPLOYEE_JOB_TITLE, EMPLOYEE_NUMBER, EMPLOYEE_SUPERVISOR_ID, EMPLOYEE_CREATED_ON, false))
                .expectEvents(EMPLOYEE_CREATED_EVENT);
    }


    @Test
    void rejectsUpdateEmployeeCommand_WhenEmployeeIsAlreadyDisabled() {
        var command = new UpdateEmployeeCommand(EMPLOYEE_ID, EMPLOYEE_USER_ID, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME,
                EMPLOYEE_DATE_OF_BIRTH, EMPLOYEE_JOB_TITLE, EMPLOYEE_NUMBER, EMPLOYEE_SUPERVISOR_ID, EMPLOYEE_CREATED_ON, false);

        testFixture.given(EMPLOYEE_CREATED_EVENT, EMPLOYEE_DISABLED_EVENT)
                .when(command)
                .expectNoEvents()
                .expectException(TranslatableIllegalStateException.class)
                .expectExceptionMessage("EMPLOYEE_IS_DISABLED");
    }

    @Test
    void rejectsUpdateEmployeeCommand_WhenNoFieldsAreBeingChanged() {
        var command = new UpdateEmployeeCommand(EMPLOYEE_ID, UUID_NO_CHANGE, NO_CHANGE, NO_CHANGE,
                DATE_NO_CHANGE, NO_CHANGE, NO_CHANGE, UUID_NO_CHANGE,
                DATE_NO_CHANGE, false);

        testFixture.given(EMPLOYEE_CREATED_EVENT)
                .when(command)
                .expectNoEvents()
                .expectException(TranslatableIllegalArgumentException.class)
                .expectExceptionMessage("EMPLOYEE_UPDATE_NO_FIELDS_CHANGED");
    }

}
