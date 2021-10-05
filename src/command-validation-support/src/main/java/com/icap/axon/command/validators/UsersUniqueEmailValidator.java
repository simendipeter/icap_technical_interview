package com.icap.axon.command.validators;

import com.icap.axon.command.validation.UserUniqueEmailValidatableCommand;
import com.icap.axon.command.validation.Validates;
import com.icap.i18n.TranslatableExceptionFactory;
import com.icap.users.services.UsersReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.icap.i18n.MessageKeys.EMAIL_ADDRESS_ALREADY_EXISTS;

@Component
public class UsersUniqueEmailValidator implements Validates<UserUniqueEmailValidatableCommand> {
    private final UsersReadService usersReadService;

    @Autowired
    public UsersUniqueEmailValidator(UsersReadService usersReadService) {
        this.usersReadService = usersReadService;
    }

    @Override
    public void validate(UserUniqueEmailValidatableCommand command) {
        if (usersReadService.hasUserWithEmail(command.getEmailAddress())) {
            TranslatableExceptionFactory.throwForKey(EMAIL_ADDRESS_ALREADY_EXISTS);
        }
    }
}
