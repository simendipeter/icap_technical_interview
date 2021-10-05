package com.icap.axon.command.validators;

import com.icap.axon.command.validation.EmailAddressValidatableCommand;
import com.icap.axon.command.validation.Validates;
import com.icap.i18n.TranslatableExceptionFactory;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

import static com.icap.i18n.MessageKeys.EMAIL_ADDRESS_MALFORMED;

@Component
public class EmailAddressValidator implements Validates<EmailAddressValidatableCommand> {

    @Override
    public void validate(EmailAddressValidatableCommand validatable) {
        if (validatable.getEmailAddress() == null) {
            return;
        }
        if (!EmailValidator.getInstance(false).isValid(validatable.getEmailAddress())) {
            TranslatableExceptionFactory.throwForKey(EMAIL_ADDRESS_MALFORMED);
        }
    }
}
