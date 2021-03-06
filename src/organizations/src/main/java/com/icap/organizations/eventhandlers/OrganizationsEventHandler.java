package com.icap.organizations.eventhandlers;

import com.icap.axon.replay.ReplayCompletionAware;
import com.icap.organizations.OrganizationAddress;
import com.icap.organizations.domain.events.OrganizationAddressUpdatedEvent;
import com.icap.organizations.domain.events.OrganizationContactDetailsUpdatedEvent;
import com.icap.organizations.domain.events.OrganizationDisabledByAdminEvent;
import com.icap.organizations.domain.events.OrganizationEnabledByAdminEvent;
import com.icap.organizations.domain.events.OrganizationNameChangedEvent;
import com.icap.organizations.domain.events.OrganizationRegisteredEvent;
import com.icap.organizations.persistence.Address;
import com.icap.organizations.persistence.OrganizationsRepository;
import lombok.extern.log4j.Log4j2;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.eventhandling.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class OrganizationsEventHandler implements ReplayCompletionAware {

    private final OrganizationsRepository organizationsRepository;

    @Autowired
    public OrganizationsEventHandler(OrganizationsRepository organizationsRepository) {
        this.organizationsRepository = organizationsRepository;
    }

    @ResetHandler
    public void prepareForReplay() {
        LOGGER.info("{} deleting projections", OrganizationsEventHandler.class.getSimpleName());
        organizationsRepository.deleteAll();
    }

    @EventHandler
    void on(OrganizationRegisteredEvent event, @Timestamp Instant creationTime) {
        LOGGER.info("Creating new registered organization {}", event.getOrganizationId());
        var organizationAddress = new OrganizationAddress(event.getStreet(), event.getCity(), event.getState(),
                event.getCountry(), event.getPostalCode());
        organizationsRepository.createOrganization(event.getOrganizationId(), event.getOrganizationName(),
                organizationAddress, event.getWebsiteUrl(), event.getContactName(), event.getContactPhoneNumber(),
                event.getContactEmail(), false, creationTime);
    }

    @EventHandler
    void on(OrganizationDisabledByAdminEvent event) {
        LOGGER.info("Organization {} disabled by {}", event.getOrganizationId(), event.getAdminId());
        var persistableOrganization = organizationsRepository.findById(event.getOrganizationId()).orElseThrow();
        persistableOrganization.setDisabled(true);
        organizationsRepository.save(persistableOrganization);
    }

    @EventHandler
    void on(OrganizationEnabledByAdminEvent event) {
        LOGGER.info("Organization {} enabled by {}", event.getOrganizationId(), event.getAdminId());
        var persistableOrganization = organizationsRepository.findById(event.getOrganizationId()).orElseThrow();
        persistableOrganization.setDisabled(false);
        organizationsRepository.save(persistableOrganization);
    }

    @EventHandler
    void on(OrganizationNameChangedEvent event) {
        LOGGER.info("Organization {} name updated by {}", event.getOrganizationId(), event.getUpdatingUserId());
        var organization = organizationsRepository.findById(event.getOrganizationId()).orElseThrow();
        organization.setOrganizationName(selectDesiredState(event.getOrganizationName(), organization.getOrganizationName()));
        organizationsRepository.save(organization);
    }

    @EventHandler
    void on(OrganizationContactDetailsUpdatedEvent event) {
        LOGGER.info("Organization {} contact details updated by {}", event.getOrganizationId(), event.getUpdatingUserId());
        var organization = organizationsRepository.findById(event.getOrganizationId()).orElseThrow();
        organization.setContactName(selectDesiredState(event.getContactName(), organization.getContactName()));
        organization.setPhoneNumber(selectDesiredState(event.getPhoneNumber(), organization.getPhoneNumber()));
        organization.setEmailAddress(selectDesiredState(event.getEmailAddress(), organization.getEmailAddress()));
        organization.setWebsiteUrl(selectDesiredState(event.getWebsiteUrl(), organization.getWebsiteUrl()));
        organizationsRepository.save(organization);
    }

    @EventHandler
    void on(OrganizationAddressUpdatedEvent event) {
        LOGGER.info("Organization {} address updated by {}", event.getOrganizationId(), event.getUpdatingUserId());
        var organization = organizationsRepository.findById(event.getOrganizationId()).orElseThrow();
        var organizationAddress = organization.getAddress();
        var city = selectDesiredState(event.getCity(), organizationAddress.getCity());
        var street = selectDesiredState(event.getStreet(), organizationAddress.getStreet());
        var state = selectDesiredState(event.getState(), organizationAddress.getState());
        var country = selectDesiredState(event.getCountry(), organizationAddress.getCountry());
        var postalCode = selectDesiredState(event.getPostalCode(), organizationAddress.getPostalCode());
        var address = new Address(street, city, state, country, postalCode);
        organization.setAddress(address);
        organizationsRepository.save(organization);
    }

    private String selectDesiredState(String desiredState, String currentState) {
        return desiredState == null ? currentState : desiredState;
    }
}
