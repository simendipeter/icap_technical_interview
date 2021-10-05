package com.icap.api.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icap.api.config.TestApiConfig;
import com.icap.api.helpers.AuthContextExtension;
import com.icap.api.rest.requests.NewEmployeeRequest;
import com.icap.organizations.services.Employee;
import com.icap.organizations.services.EmployeeReadService;
import com.icap.organizations.services.EmployeeService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static java.util.UUID.fromString;
import static org.assertj.core.util.Lists.newArrayList;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = {TestApiConfig.class, AdminOrganizationsController.class})
@AutoConfigureMockMvc
@ExtendWith({MockitoExtension.class, SpringExtension.class, AuthContextExtension.class})
class EmployeeControllerTest {

    private static final String ADMIN_USERNAME = "peter.simendi@icap.com";
    private static final String ROLE_ADMIN = "ADMIN";
    private static final Employee EMPLOYEE_1 = new Employee(fromString("53ac29ab-ecc6-431e-bde0-64440cd3dc93"),
            fromString("53ac29ab-ecc6-431e-bde0-64440cd3dc94"),"Peter", "Simendi", Instant.now(),
            "Software Developer", "12345566", fromString("53ac29ab-ecc6-431e-bde0-64440cd3dc93"),
            Instant.now(), false);
    private static final Employee EMPLOYEE_2 = new Employee(fromString("53ac29ab-ecc6-431e-bde0-64440cd3dd93"),
            fromString("53ac29ab-ecc6-432e-bde0-64440cd3dc56"),"John", "Doe", Instant.now(),
            "Software Developer", "76766767", fromString("65ac29ab-ecc6-431e-bde0-64440cd3dc72"),
            Instant.now(), false);


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;
    @MockBean
    private EmployeeReadService employeeReadService;

    @Test
    @WithMockUser(username = ADMIN_USERNAME, roles = ROLE_ADMIN)
    void getEmployeesWillRetrieveListOfOEmployees_WhenRequestingUserIsAdmin() throws Exception {
        when(employeeReadService.getEmployees())
                .thenReturn(newArrayList(EMPLOYEE_1, EMPLOYEE_2));

        mockMvc.perform(get("/api/employees").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(EMPLOYEE_1.getId().toString())))
                .andExpect(jsonPath("$.[1].id", is(EMPLOYEE_2.getId().toString())))
                .andExpect(jsonPath("$.[0].firstName", is(EMPLOYEE_1.getFirstName())))
                .andExpect(jsonPath("$.[1].firstName", is(EMPLOYEE_2.getFirstName())));
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, roles = ROLE_ADMIN)
    void creatingRegisteredEmployeeWillFail_WhenUserIdIsEmpty() throws Exception {
        mockMvc.perform(post("/api/employees")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new NewEmployeeRequest(null, EMPLOYEE_1.getFirstName(),
                        EMPLOYEE_1.getLastName(), EMPLOYEE_1.getDateOfBirth(), EMPLOYEE_1.getJobTitle(), EMPLOYEE_1.getEmployeeNumber(),
                        EMPLOYEE_1.getImmediateSupervisorId(), EMPLOYEE_1.getRegisteredOn(), EMPLOYEE_1.isDisabled()))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(employeeService);
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, roles = ROLE_ADMIN)
    void creatingRegisteredEmployeeWillDelegate_WhenRequestingUserIsAdmin() throws Exception {
        mockMvc.perform(post("/api/employees")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new NewEmployeeRequest(EMPLOYEE_1.getUserId(), EMPLOYEE_1.getFirstName(),
                        EMPLOYEE_1.getLastName(), EMPLOYEE_1.getDateOfBirth(), EMPLOYEE_1.getJobTitle(), EMPLOYEE_1.getEmployeeNumber(),
                        EMPLOYEE_1.getImmediateSupervisorId(), EMPLOYEE_1.getRegisteredOn(), EMPLOYEE_1.isDisabled()))))
                .andExpect(status().isCreated())
                .andExpect(content().string(Matchers.any(String.class)));

        verify(employeeService).createRegisteredEmployee(EMPLOYEE_1.getUserId(), EMPLOYEE_1.getFirstName(),
                EMPLOYEE_1.getLastName(), EMPLOYEE_1.getDateOfBirth(), EMPLOYEE_1.getJobTitle(), EMPLOYEE_1.getEmployeeNumber(),
                EMPLOYEE_1.getImmediateSupervisorId(), EMPLOYEE_1.getRegisteredOn(), EMPLOYEE_1.isDisabled());
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, roles = ROLE_ADMIN)
    void disableOrganizationWillDelegate_WhenRequestingUserIsAdmin() throws Exception {
        mockMvc.perform(delete("/api/employees/{employeeId}", EMPLOYEE_2.getId()))
                .andExpect(status().isOk());

        verify(employeeService).disableEmployee(EMPLOYEE_2.getId());
    }

    @Test
    @WithMockUser(username = ADMIN_USERNAME, roles = ROLE_ADMIN)
    void enableOrganizationWillDelegate_WhenRequestingUserIsAdmin() throws Exception {
        mockMvc.perform(post("/api/employees/{employeeId}", EMPLOYEE_2.getId()))
                .andExpect(status().isOk());

        verify(employeeService).enableEmployee(EMPLOYEE_2.getId());
    }
}
