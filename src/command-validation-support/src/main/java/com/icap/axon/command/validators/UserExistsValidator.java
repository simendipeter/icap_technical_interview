package com.icap.axon.command.validators;

import com.icap.axon.command.validation.UserExistsValidatableCommand;
import com.icap.axon.command.validation.Validates;
import com.icap.i18n.TranslatableExceptionFactory;
import com.icap.users.services.UsersReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.icap.i18n.MessageKeys.USER_NOT_FOUND;


@Component
public class UserExistsValidator implements Validates<UserExistsValidatableCommand> {
    private final UsersReadService usersReadService;

    @Autowired
    public UserExistsValidator(UsersReadService usersReadService) {
        this.usersReadService = usersReadService;
    }


    @Override
    public void validate(UserExistsValidatableCommand command) {
        if (!usersReadService.exists(command.getUserIdId())) {
            TranslatableExceptionFactory.throwForKey(USER_NOT_FOUND);
        }

    }
}
