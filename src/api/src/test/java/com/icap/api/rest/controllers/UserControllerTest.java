package com.icap.api.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icap.api.config.TestApiConfig;
import com.icap.api.helpers.AuthContextExtension;
import com.icap.api.helpers.MockAuthenticationContextProvider;
import com.icap.api.rest.requests.UpdateUserRequest;
import com.icap.axon.common.domain.User;
import com.icap.users.services.UsersReadService;
import com.icap.users.services.UsersService;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = {TestApiConfig.class, UserController.class})
@AutoConfigureMockMvc
@ExtendWith({MockitoExtension.class, SpringExtension.class, AuthContextExtension.class})
class UserControllerTest {

    private static final String ROLE_ORGANIZATION_USER = "ORG_USER";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UsersService usersService;
    @MockBean
    private UsersReadService usersReadService;

    @Test
    @WithMockUser(username = "user@umbrella.com", roles = ROLE_ORGANIZATION_USER)
    void willGetUserInfo() throws Exception {
        User authUser = MockAuthenticationContextProvider.getAuthUser();
        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(authUser.getId().toString())));
    }

    @Test
    @WithMockUser(username = "user@umbrella.com", roles = ROLE_ORGANIZATION_USER)
    void willUpdateUserInfo() throws Exception {
        User authUser = MockAuthenticationContextProvider.getAuthUser();
        mockMvc.perform(put("/api/user")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UpdateUserRequest("display-name-change", "email-change", "password-change"))))
                .andExpect(status().isOk());

        verify(usersService).updateUser(authUser.getId(), authUser.getId(), "email-change",
                "display-name-change", "password-change");
    }
}
