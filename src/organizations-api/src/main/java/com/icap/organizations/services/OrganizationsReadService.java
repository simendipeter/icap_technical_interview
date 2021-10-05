package com.icap.organizations.services;

import com.icap.axon.common.services.ReadService;
import com.icap.organizations.Organization;

import java.util.List;
import java.util.UUID;

public interface OrganizationsReadService extends ReadService<Organization> {

    List<Organization> getOrganizations();

    boolean exists(UUID organizationId);
}
