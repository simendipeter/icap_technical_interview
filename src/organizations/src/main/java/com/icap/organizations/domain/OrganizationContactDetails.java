package com.icap.organizations.domain;

import com.icap.organizations.domain.events.OrganizationAddressUpdatedEvent;
import com.icap.organizations.domain.events.OrganizationContactDetailsUpdatedEvent;
import com.icap.organizations.domain.events.OrganizationRegisteredEvent;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.EntityId;

import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode
@ToString
@NoArgsConstructor
class OrganizationContactDetails implements Serializable {

    @EntityId
    private UUID organizationId;
    private String websiteUrl;
    private String contactName;
    private String contactPhoneNumber;
    private String contactEmail;
    private String street;
    private String city;
    private String state;
    private String country;
    private String postalCode;

    @EventSourcingHandler
    void on(OrganizationRegisteredEvent event) {
        contactName = event.getContactName();
        contactEmail = event.getContactEmail();
        contactPhoneNumber = event.getContactPhoneNumber();
        websiteUrl = event.getWebsiteUrl();
    }

    @EventSourcingHandler
    void on(OrganizationContactDetailsUpdatedEvent event) {
        contactName = event.getContactName();
        contactEmail = event.getEmailAddress();
        contactPhoneNumber = event.getPhoneNumber();
        websiteUrl = event.getWebsiteUrl();
    }

    @EventSourcingHandler
    void on(OrganizationAddressUpdatedEvent event) {
        city = event.getCity();
        country = event.getCountry();
        postalCode = event.getPostalCode();
        state = event.getState();
        street = event.getStreet();
    }
}
