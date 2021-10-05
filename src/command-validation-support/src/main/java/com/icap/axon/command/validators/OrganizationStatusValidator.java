package com.icap.axon.command.validators;

import com.icap.axon.command.validation.OrganizationStatusValidatableCommand;
import com.icap.axon.command.validation.Validates;
import com.icap.i18n.TranslatableExceptionFactory;
import com.icap.organizations.Organization;
import com.icap.organizations.services.OrganizationsReadService;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

import static com.icap.i18n.MessageKeys.ORGANIZATION_DOES_NOT_EXIST;
import static com.icap.i18n.MessageKeys.ORGANIZATION_IS_DEREGISTERED;

@Component
public class OrganizationStatusValidator implements Validates<OrganizationStatusValidatableCommand> {

    private final OrganizationsReadService organizationsReadService;

    public OrganizationStatusValidator(OrganizationsReadService organizationsReadService) {
        this.organizationsReadService = organizationsReadService;
    }

    @Override
    public void validate(OrganizationStatusValidatableCommand command) {
        Organization organization;
        try {
            organization = organizationsReadService.getById(command.getOrganizationId());
            if (organization.isDisabled()) {
                TranslatableExceptionFactory.throwForKey(ORGANIZATION_IS_DEREGISTERED, command.getOrganizationId());
            }
        } catch (NoSuchElementException e) {
            TranslatableExceptionFactory.throwForKey(ORGANIZATION_DOES_NOT_EXIST, e, command.getOrganizationId());
        }
    }
}
