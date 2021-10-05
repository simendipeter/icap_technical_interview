package com.icap.users.domain.commands;

import com.icap.axon.command.validation.EmailAddressValidatableCommand;
import com.icap.axon.command.validation.UserUniqueEmailValidatableCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDetailsCommand implements EmailAddressValidatableCommand, UserUniqueEmailValidatableCommand {

    @TargetAggregateIdentifier
    private UUID userId;
    private String emailChange;
    private String displayNameChange;
    private String passwordChange;

    @NotNull
    private UUID requestingUserId;

    @Override
    public String getEmailAddress() {
        return emailChange;
    }

}
