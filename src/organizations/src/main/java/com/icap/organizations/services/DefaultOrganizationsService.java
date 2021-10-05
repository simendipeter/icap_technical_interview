package com.icap.organizations.services;


import com.icap.axon.common.RandomFieldsGenerator;
import com.icap.organizations.domain.commands.CreateRegisteredOrganizationCommand;
import com.icap.organizations.domain.commands.DisableOrganizationCommand;
import com.icap.organizations.domain.commands.EnableOrganizationCommand;
import com.icap.organizations.domain.commands.UpdateOrganizationCommand;
import engineering.everest.axon.HazelcastCommandGateway;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DefaultOrganizationsService implements OrganizationsService {

    private final RandomFieldsGenerator randomFieldsGenerator;
    private final HazelcastCommandGateway commandGateway;

    public DefaultOrganizationsService(RandomFieldsGenerator randomFieldsGenerator, HazelcastCommandGateway commandGateway) {
        this.randomFieldsGenerator = randomFieldsGenerator;
        this.commandGateway = commandGateway;
    }

    @Override
    public void updateOrganization(UUID requestingUserId, UUID organizationId, String organizationName,
                                   String street, String city, String state, String country, String postalCode,
                                   String websiteUrl, String contactName, String phoneNumber, String emailAddress) {
        commandGateway.sendAndWait(new UpdateOrganizationCommand(organizationId, requestingUserId, organizationName, street, city,
                state, country, postalCode, websiteUrl, contactName, phoneNumber, emailAddress));
    }

    @Override
    public UUID createRegisteredOrganization(UUID requestingUserId, String organizationName, String street, String city, String state,
                                             String country, String postalCode, String websiteUrl, String contactName, String phoneNumber,
                                             String emailAddress) {
        UUID organizationId = randomFieldsGenerator.genRandomUUID();
        return commandGateway.sendAndWait(new CreateRegisteredOrganizationCommand(organizationId,
                requestingUserId, organizationName, street, city, state, country, postalCode, websiteUrl, contactName,
                phoneNumber, emailAddress));
    }

    @Override
    public void disableOrganization(UUID requestingUserId, UUID organizationId) {
        commandGateway.sendAndWait(new DisableOrganizationCommand(organizationId, requestingUserId));
    }

    @Override
    public void enableOrganization(UUID requestingUserId, UUID organizationId) {
        commandGateway.sendAndWait(new EnableOrganizationCommand(organizationId, requestingUserId));
    }
}
