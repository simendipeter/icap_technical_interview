package com.icap.api.rest.converters;

import com.icap.api.rest.responses.EmployeeResponse;
import com.icap.api.rest.responses.OrganizationResponse;
import com.icap.api.rest.responses.UserResponse;
import com.icap.axon.common.domain.User;
import com.icap.organizations.Organization;
import com.icap.organizations.services.Employee;
import org.springframework.stereotype.Service;

@Service
public class DtoConverter {

    public UserResponse convert(User user) {
        return new UserResponse(user.getId(), user.getOrganizationId(), user.getUsername(), user.getDisplayName(),
                user.getEmail(), user.isDisabled(), user.getRoles());
    }

    public OrganizationResponse convert(Organization organization) {
        var address = organization.getOrganizationAddress();
        return new OrganizationResponse(organization.getId(), organization.getOrganizationName(),
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getCountry(),
                address.getPostalCode(),
                organization.getWebsiteUrl(),
                organization.getContactName(),
                organization.getPhoneNumber(),
                organization.getEmailAddress(),
                organization.isDisabled());
    }

    public EmployeeResponse convert(Employee employee) {
        return new EmployeeResponse(employee.getId(), employee.getUserId(), employee.getFirstName(), employee.getLastName(),
                employee.getDateOfBirth(), employee.getJobTitle(), employee.getEmployeeNumber(), employee.getImmediateSupervisorId(),
                employee.getRegisteredOn(), employee.isDisabled());
    }
}

