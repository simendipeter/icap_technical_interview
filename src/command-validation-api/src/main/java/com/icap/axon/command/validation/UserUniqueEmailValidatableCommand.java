package com.icap.axon.command.validation;

public interface UserUniqueEmailValidatableCommand extends ValidatableCommand {

    String getEmailAddress();
}
