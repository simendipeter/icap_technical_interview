package com.icap.organizations.domain.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.serialization.Revision;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Revision("0")
public class OrganizationAddressUpdatedEvent {
    private UUID organizationId;
    private String street;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private UUID updatingUserId;
}
