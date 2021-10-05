package com.icap.users.services;

import com.icap.axon.common.domain.User;
import com.icap.axon.common.services.ReadService;

import java.util.List;
import java.util.UUID;

public interface UsersReadService extends ReadService<User> {

    List<User> getUsers();

    List<User> getUsersForOrganization(UUID organizationId);

    boolean exists(UUID userId);

    User getUserByUsername(String username);

    boolean hasUserWithEmail(String email);

}
