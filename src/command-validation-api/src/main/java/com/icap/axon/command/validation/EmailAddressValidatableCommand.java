package com.icap.axon.command.validation;

public interface EmailAddressValidatableCommand extends ValidatableCommand {

    String getEmailAddress();
}
