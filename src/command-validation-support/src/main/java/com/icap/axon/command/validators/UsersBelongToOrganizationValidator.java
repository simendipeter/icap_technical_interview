package com.icap.axon.command.validators;

import com.icap.axon.command.validation.UsersBelongToOrganizationValidatableCommand;
import com.icap.axon.command.validation.Validates;
import com.icap.i18n.TranslatableExceptionFactory;
import com.icap.users.services.UsersReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.icap.i18n.MessageKeys.USER_NOT_MEMBER_OF_ORGANIZATION;

@Component
public class UsersBelongToOrganizationValidator implements Validates<UsersBelongToOrganizationValidatableCommand> {

    private final UsersReadService usersReadService;

    @Autowired
    public UsersBelongToOrganizationValidator(UsersReadService usersReadService) {
        this.usersReadService = usersReadService;
    }

    @Override
    public void validate(UsersBelongToOrganizationValidatableCommand validatable) {
        for (UUID userId : validatable.getUserIds()) {
            var userOrganizationId = usersReadService.getById(userId).getOrganizationId();
            if (!validatable.getOrganizationId().equals(userOrganizationId)) {
                TranslatableExceptionFactory.throwForKey(USER_NOT_MEMBER_OF_ORGANIZATION, userId);
            }
        }
    }
}
