package com.icap.users.eventhandlers;

import com.icap.axon.replay.ReplayCompletionAware;
import com.icap.organizations.domain.events.UserPromotedToOrganizationAdminEvent;
import com.icap.users.domain.events.UserCreatedByAdminEvent;
import com.icap.users.domain.events.UserCreatedForNewlyRegisteredOrganizationEvent;
import com.icap.users.domain.events.UserDeletedAndForgottenEvent;
import com.icap.users.domain.events.UserDetailsUpdatedByAdminEvent;
import com.icap.users.persistence.UsersRepository;
import engineering.everest.axon.cryptoshredding.CryptoShreddingKeyService;
import engineering.everest.axon.cryptoshredding.TypeDifferentiatedSecretKeyId;
import lombok.extern.log4j.Log4j2;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.eventhandling.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.icap.axon.common.domain.Role.ORG_ADMIN;
import static com.icap.axon.common.domain.User.ADMIN_ID;

@Service
@Log4j2
public class UsersEventHandler implements ReplayCompletionAware {

    private final UsersRepository usersRepository;
    private final CryptoShreddingKeyService cryptoShreddingKeyService;

    @Autowired
    public UsersEventHandler(UsersRepository usersRepository, CryptoShreddingKeyService cryptoShreddingKeyService) {
        this.usersRepository = usersRepository;
        this.cryptoShreddingKeyService = cryptoShreddingKeyService;
    }

    @ResetHandler
    public void prepareForReplay() {
        LOGGER.info("{} deleting projections", UsersEventHandler.class.getSimpleName());
        usersRepository.deleteByIdNot(ADMIN_ID);
    }

    @EventHandler
    void on(UserCreatedByAdminEvent event, @Timestamp Instant creationTime) {
        LOGGER.info("User {} created for admin created organization {}", event.getUserId(), event.getOrganizationId());
        var userEmail = event.getUserEmail() == null ? String.format("%s@deleted", event.getUserId()) : event.getUserEmail();
        usersRepository.createUser(event.getUserId(), event.getOrganizationId(), event.getUserDisplayName(),
                userEmail, event.getEncodedPassword(), creationTime);
    }

    @EventHandler
    void on(UserCreatedForNewlyRegisteredOrganizationEvent event, @Timestamp Instant creationTime) {
        LOGGER.info("User {} created for self registered organization {}", event.getUserId(), event.getOrganizationId());
        usersRepository.createUser(event.getUserId(), event.getOrganizationId(), event.getUserDisplayName(),
                event.getUserEmail(), event.getEncodedPassword(), creationTime);

        // You may also want a non-replayable event handler for sending a welcome email to new users
    }

    @EventHandler
    void on(UserDetailsUpdatedByAdminEvent event) {
        LOGGER.info("User {} details updated by admin {}", event.getUserId(), event.getAdminId());
        var persistableUser = usersRepository.findById(event.getUserId()).orElseThrow();
        persistableUser.setDisplayName(selectDesiredState(event.getDisplayNameChange(), persistableUser.getDisplayName()));
        persistableUser.setEmail(selectDesiredState(event.getEmailChange(), persistableUser.getEmail()));
        persistableUser.setEncodedPassword(selectDesiredState(event.getEncodedPasswordChange(), persistableUser.getEncodedPassword()));
        usersRepository.save(persistableUser);
    }


    @EventHandler
    void on(UserPromotedToOrganizationAdminEvent event) {
        LOGGER.info("Adding role {} to user {}", ORG_ADMIN, event.getPromotedUserId());
        var persistableUser = usersRepository.findById(event.getPromotedUserId()).orElseThrow();
        persistableUser.addRole(ORG_ADMIN);
        usersRepository.save(persistableUser);
    }

    @EventHandler
    void on(UserDeletedAndForgottenEvent event) {
        LOGGER.info("Deleting user {}", event.getDeletedUserId());
        usersRepository.deleteById(event.getDeletedUserId());
        cryptoShreddingKeyService.deleteSecretKey(new TypeDifferentiatedSecretKeyId(event.getDeletedUserId().toString(), ""));
    }

    private String selectDesiredState(String desiredState, String currentState) {
        return desiredState == null ? currentState : desiredState;
    }
}
