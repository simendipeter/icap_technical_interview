package com.icap.functionaltests.scenarios;

import com.icap.AdminProvisionTask;
import com.icap.Launcher;
import com.icap.api.rest.requests.NewOrganizationRequest;
import com.icap.api.rest.requests.NewUserRequest;
import com.icap.functionaltests.helpers.ApiRestTestClient;
import com.icap.functionaltests.helpers.TestEventHandler;
import com.icap.functionaltests.helpers.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;
import java.util.UUID;

import static java.lang.Boolean.FALSE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = Launcher.class)
@ActiveProfiles("standalone")
class ReplayFunctionalTests {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private AdminProvisionTask adminProvisionTask;
    @Autowired
    private TestEventHandler testEventHandler;

    private ApiRestTestClient apiRestTestClient;

    @BeforeEach
    void setUp() {
        apiRestTestClient = new ApiRestTestClient(webTestClient, adminProvisionTask);
        apiRestTestClient.createAdminUserAndLogin();
    }

    @Test
    void canGetReplayStatus() {
        Map<String, Object> replayStatus = apiRestTestClient.getReplayStatus(OK);
        assertSame(FALSE, replayStatus.get("isReplaying"));
    }

    @Test
    void canTriggerReplayEvents() {
        apiRestTestClient.triggerReplay(NO_CONTENT);
        // This is necessary, otherwise the canGetReplayStatus() may sometimes fail if it runs later
        // and things happen too fast
        TestUtils.assertOk(() -> assertSame(FALSE, apiRestTestClient.getReplayStatus(OK).get("isReplaying")));
    }

    @Test
    @Disabled("To be revisited. This test always passes locally yet fails on the new build node")
    void replayStatusWillBeSetCorrectlyForReplay() {
        // First event and replay marker event
        UUID org1 = apiRestTestClient.createRegisteredOrganization(
                new NewOrganizationRequest("test org", null, null, null, null, null, null, null, null, "admin@example.com"),
                CREATED);
        apiRestTestClient.createUser(org1, new NewUserRequest("alice@umbrella.com", "password", "Alice"), CREATED);
        apiRestTestClient.triggerReplay(NO_CONTENT);
        TestUtils.assertOk(() -> assertSame(FALSE, apiRestTestClient.getReplayStatus(OK).get("isReplaying")));
        assertEquals(1, testEventHandler.getCounter().get());

        // Second event and replay again
        UUID org2 = apiRestTestClient.createRegisteredOrganization(
                new NewOrganizationRequest("test org", null, null, null, null, null, null, null, null, "admin2@example.com"),
                CREATED);
        apiRestTestClient.createUser(org2, new NewUserRequest("bob@umbrella.com", "password", "Bob"), CREATED);
        apiRestTestClient.triggerReplay(NO_CONTENT);
        TestUtils.assertOk(() -> assertSame(FALSE, apiRestTestClient.getReplayStatus(OK).get("isReplaying")));
        assertEquals(2, testEventHandler.getCounter().get());

        // We should have 2 organisations in total
        assertEquals(2, apiRestTestClient.getAllOrganizations(OK).size());

        // and 3 users in total (2 created above and 1 admin)
        assertEquals(3, apiRestTestClient.getAllUsers(OK).size());
    }
}
