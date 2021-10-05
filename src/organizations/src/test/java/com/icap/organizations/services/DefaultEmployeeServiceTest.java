package com.icap.organizations.services;

import com.icap.axon.common.RandomFieldsGenerator;
import com.icap.organizations.domain.commands.CreateRegisteredEmployeeCommand;
import com.icap.organizations.domain.commands.DisableEmployeeCommand;
import com.icap.organizations.domain.commands.EnableEmployeeCommand;
import com.icap.organizations.domain.commands.UpdateEmployeeCommand;
import engineering.everest.axon.HazelcastCommandGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultEmployeeServiceTest {

    private static final UUID EMPLOYEE_ID = randomUUID();
    private static final UUID EMPLOYEE_USER_ID= randomUUID();
    private static final String EMPLOYEE_FIRST_NAME = "Peter";
    private static final String EMPLOYEE_LAST_NAME = "Simendi";;
    private static final Instant EMPLOYEE_DATE_OF_BIRTH = Instant.ofEpochSecond(100L);
    private static final String EMPLOYEE_JOB_TITLE = "Software Developer";
    private static final String EMPLOYEE_NUMBER = "12345";
    private static final UUID EMPLOYEE_SUPERVISOR_ID = randomUUID();
    private static final Instant EMPLOYEE_CREATED_ON = Instant.ofEpochSecond(100L);

    @Mock
    private RandomFieldsGenerator randomFieldsGenerator;
    @Mock
    private HazelcastCommandGateway commandGateway;

    private DefaultEmployeeService defaultEmployeeService;

    @BeforeEach
    void setUp() {
        defaultEmployeeService = new DefaultEmployeeService(randomFieldsGenerator, commandGateway);
    }

    @Test
    void createRegisteredEmployee_WillSendCommandAndWaitForCompletion() {
        var expectedCommand = new CreateRegisteredEmployeeCommand(EMPLOYEE_ID, EMPLOYEE_USER_ID, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME, EMPLOYEE_DATE_OF_BIRTH,
                EMPLOYEE_JOB_TITLE, EMPLOYEE_NUMBER, EMPLOYEE_SUPERVISOR_ID, EMPLOYEE_CREATED_ON, false);

        when(randomFieldsGenerator.genRandomUUID()).thenReturn(EMPLOYEE_ID);
        when(commandGateway.sendAndWait(expectedCommand)).thenReturn(EMPLOYEE_ID);

        var newEmployeeId = defaultEmployeeService.createRegisteredEmployee(EMPLOYEE_USER_ID, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME, EMPLOYEE_DATE_OF_BIRTH,
                EMPLOYEE_JOB_TITLE, EMPLOYEE_NUMBER, EMPLOYEE_SUPERVISOR_ID, EMPLOYEE_CREATED_ON, false);

        assertEquals(EMPLOYEE_ID, newEmployeeId);
        verify(commandGateway).sendAndWait(expectedCommand);
    }

    @Test
    void updateEmployee_WillSendCommandAndWaitForCompletion() {
        defaultEmployeeService.updateEmployee(EMPLOYEE_ID, EMPLOYEE_USER_ID, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME, EMPLOYEE_DATE_OF_BIRTH,
                EMPLOYEE_JOB_TITLE, EMPLOYEE_NUMBER, EMPLOYEE_SUPERVISOR_ID, EMPLOYEE_CREATED_ON, false);

        verify(commandGateway).sendAndWait(new UpdateEmployeeCommand(EMPLOYEE_ID, EMPLOYEE_USER_ID, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME, EMPLOYEE_DATE_OF_BIRTH,
                EMPLOYEE_JOB_TITLE, EMPLOYEE_NUMBER, EMPLOYEE_SUPERVISOR_ID, EMPLOYEE_CREATED_ON, false));
    }

    @Test
    void enableEmployee_WillSendCommandAndWaitForCompletion() {
        defaultEmployeeService.enableEmployee(EMPLOYEE_ID);
        verify(commandGateway).sendAndWait(new EnableEmployeeCommand(EMPLOYEE_ID));
    }

    @Test
    void disableEmployee_WillSendCommandAndWaitForCompletion() {
        defaultEmployeeService.disableEmployee(EMPLOYEE_ID);
        verify(commandGateway).sendAndWait(new DisableEmployeeCommand(EMPLOYEE_ID));
    }
}
