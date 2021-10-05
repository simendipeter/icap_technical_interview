package com.icap.users.services;

import com.icap.axon.common.RandomFieldsGenerator;
import com.icap.users.domain.commands.CreateUserCommand;
import com.icap.users.domain.commands.DeleteAndForgetUserCommand;
import com.icap.users.domain.commands.UpdateUserDetailsCommand;
import engineering.everest.axon.HazelcastCommandGateway;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
public class DefaultUsersService implements UsersService {

    private final HazelcastCommandGateway commandGateway;
    private final RandomFieldsGenerator randomFieldsGenerator;
    private final PasswordEncoder passwordEncoder;

    public DefaultUsersService(HazelcastCommandGateway commandGateway,
                               RandomFieldsGenerator randomFieldsGenerator,
                               PasswordEncoder passwordEncoder) {
        this.commandGateway = commandGateway;
        this.randomFieldsGenerator = randomFieldsGenerator;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void updateUser(UUID requestingUserId, UUID userId, String emailChange,
                           String displayNameChange, String passwordChange) {

        commandGateway.sendAndWait(new UpdateUserDetailsCommand(userId, emailChange,
                displayNameChange, encodePasswordIfNotBlank(passwordChange), requestingUserId));
    }

    @Override
    public UUID createUser(UUID requestingUserId, UUID organizationId, String username, String displayName, String rawPassword) {
        return commandGateway.sendAndWait(new CreateUserCommand(randomFieldsGenerator.genRandomUUID(), organizationId, requestingUserId,
                username, encodePasswordIfNotBlank(rawPassword), displayName));
    }

    @Override
    public void deleteAndForget(UUID requestingUserId, UUID userId, String requestReason) {
        commandGateway.sendAndWait(new DeleteAndForgetUserCommand(userId, requestingUserId, requestReason));
    }

    private String encodePasswordIfNotBlank(String passwordChange) {
        return isBlank(passwordChange) ? passwordChange : passwordEncoder.encode(passwordChange);
    }
}
