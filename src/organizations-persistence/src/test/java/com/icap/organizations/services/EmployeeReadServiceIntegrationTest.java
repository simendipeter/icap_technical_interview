package com.icap.organizations.services;

import com.icap.organizations.persistence.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ComponentScan(basePackages = "com.icap.organizations")
@EntityScan(basePackages = "com.icap.organizations.persistence")
@EnableJpaRepositories(basePackages = "com.icap.organizations.persistence")
@ContextConfiguration(classes = TestProjectionsJpaConfig.class)
class EmployeeReadServiceIntegrationTest {

    private static final UUID EMPLOYEE_ID_1 = randomUUID();
    private static final UUID EMPLOYEE_ID_2 = randomUUID();
    private static final UUID EMPLOYEE_ID_3 = randomUUID();
    private static final UUID EMPLOYEE_USER_ID_1 = randomUUID();
    private static final UUID EMPLOYEE_USER_ID_2 = randomUUID();
    private static final UUID EMPLOYEE_USER_ID_3 = randomUUID();
    private static final String EMPLOYEE_FIRST_NAME_1 = "Peter";
    private static final String EMPLOYEE_FIRST_NAME_2 = "Aleta";
    private static final String EMPLOYEE_FIRST_NAME_3 = "Inzwirashe";
    private static final String EMPLOYEE_LAST_NAME_1 = "Simendi";
    private static final String EMPLOYEE_LAST_NAME_2 = "Gwaringa";
    private static final String EMPLOYEE_LAST_NAME_3 = "Simendi";
    private static final Instant EMPLOYEE_DATE_OF_BIRTH_1 = Instant.ofEpochSecond(100L);
    private static final Instant EMPLOYEE_DATE_OF_BIRTH_2 = Instant.ofEpochSecond(700L);
    private static final Instant EMPLOYEE_DATE_OF_BIRTH_3 = Instant.ofEpochSecond(5500L);
    private static final String EMPLOYEE_JOB_TITLE_1= "Software Developer";
    private static final String EMPLOYEE_JOB_TITLE_2= "Software Engineer";
    private static final String EMPLOYEE_JOB_TITLE_3= "Software Architect";
    private static final String EMPLOYEE_NUMBER_1 = "12345";
    private static final String EMPLOYEE_NUMBER_2 = "56789";
    private static final String EMPLOYEE_NUMBER_3 = "78535";
    private static final UUID EMPLOYEE_SUPERVISOR_ID_1 = randomUUID();
    private static final UUID EMPLOYEE_SUPERVISOR_ID_2 = randomUUID();
    private static final UUID EMPLOYEE_SUPERVISOR_ID_3 = randomUUID();
    private static final Instant EMPLOYEE_CREATED_ON_1 = Instant.ofEpochSecond(100L);
    private static final Instant EMPLOYEE_CREATED_ON_2 = Instant.ofEpochSecond(700L);
    private static final Instant EMPLOYEE_CREATED_ON_3 = Instant.ofEpochSecond(5500L);



    private static final Employee EMPLOYEE_1 = new Employee(EMPLOYEE_ID_1, EMPLOYEE_USER_ID_1, EMPLOYEE_FIRST_NAME_1, EMPLOYEE_LAST_NAME_1, EMPLOYEE_DATE_OF_BIRTH_1, EMPLOYEE_JOB_TITLE_1, EMPLOYEE_NUMBER_1, EMPLOYEE_SUPERVISOR_ID_1, EMPLOYEE_CREATED_ON_1, false);
    private static final Employee EMPLOYEE_2 = new Employee(EMPLOYEE_ID_2, EMPLOYEE_USER_ID_2, EMPLOYEE_FIRST_NAME_2, EMPLOYEE_LAST_NAME_2, EMPLOYEE_DATE_OF_BIRTH_2, EMPLOYEE_JOB_TITLE_2, EMPLOYEE_NUMBER_2, EMPLOYEE_SUPERVISOR_ID_2, EMPLOYEE_CREATED_ON_2, true);
    private static final Employee EMPLOYEE_3 = new Employee(EMPLOYEE_ID_3, EMPLOYEE_USER_ID_3, EMPLOYEE_FIRST_NAME_3, EMPLOYEE_LAST_NAME_3, EMPLOYEE_DATE_OF_BIRTH_3, EMPLOYEE_JOB_TITLE_3, EMPLOYEE_NUMBER_3, EMPLOYEE_SUPERVISOR_ID_3, EMPLOYEE_CREATED_ON_3, false);

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DefaultEmployeeReadService employeeReadService;

    @BeforeEach
    void setUp() {
        employeeRepository.createEmployee(EMPLOYEE_ID_1, EMPLOYEE_USER_ID_1, EMPLOYEE_FIRST_NAME_1, EMPLOYEE_LAST_NAME_1, EMPLOYEE_DATE_OF_BIRTH_1, EMPLOYEE_JOB_TITLE_1, EMPLOYEE_NUMBER_1, EMPLOYEE_SUPERVISOR_ID_1, EMPLOYEE_CREATED_ON_1, false);
        employeeRepository.createEmployee(EMPLOYEE_ID_2, EMPLOYEE_USER_ID_2, EMPLOYEE_FIRST_NAME_2, EMPLOYEE_LAST_NAME_2, EMPLOYEE_DATE_OF_BIRTH_2, EMPLOYEE_JOB_TITLE_2, EMPLOYEE_NUMBER_2, EMPLOYEE_SUPERVISOR_ID_2, EMPLOYEE_CREATED_ON_2, true);
        employeeRepository.createEmployee(EMPLOYEE_ID_3, EMPLOYEE_USER_ID_3, EMPLOYEE_FIRST_NAME_3, EMPLOYEE_LAST_NAME_3, EMPLOYEE_DATE_OF_BIRTH_3, EMPLOYEE_JOB_TITLE_3, EMPLOYEE_NUMBER_3, EMPLOYEE_SUPERVISOR_ID_3, EMPLOYEE_CREATED_ON_3, false);
    }

    @Test
    void exists_WillBeTrue_WhenEmployeeExists() {
        assertTrue(employeeReadService.exists(EMPLOYEE_ID_1));
    }

    @Test
    void exists_WillBeFalse_WhenEmployeeDoesNotExist() {
        assertFalse(employeeReadService.exists(randomUUID()));
    }

    @Test
    void getEmployeeList_WillReturnAllEmployees() {
        assertEquals(asList(EMPLOYEE_1, EMPLOYEE_2, EMPLOYEE_3),
                employeeReadService.getEmployees());
    }

    @Test
    void getEmployee_WillReturnEmployee_WhenExists() {
        assertEquals(EMPLOYEE_1, employeeReadService.getById(EMPLOYEE_ID_1));
    }

    @Test
    void getEmployee_WillFail_WhenEmployeeDoesNotExist() {
        assertThrows(NoSuchElementException.class, () -> employeeReadService.getById(randomUUID()));
    }
}
