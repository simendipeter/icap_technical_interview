package com.icap.api.rest.controllers;

import com.icap.api.rest.annotations.AdminOrAdminOfTargetOrganization;
import com.icap.api.rest.annotations.AdminOrUserOfTargetOrganization;
import com.icap.api.rest.converters.DtoConverter;
import com.icap.api.rest.requests.NewUserRequest;
import com.icap.api.rest.requests.UpdateOrganizationRequest;
import com.icap.api.rest.responses.OrganizationResponse;
import com.icap.api.rest.responses.UserResponse;
import com.icap.axon.common.RandomFieldsGenerator;
import com.icap.axon.common.domain.User;
import com.icap.organizations.services.OrganizationsReadService;
import com.icap.organizations.services.OrganizationsService;
import com.icap.users.services.UsersReadService;
import com.icap.users.services.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/organizations")
@Api(consumes = APPLICATION_JSON_VALUE, tags = "Organizations")
public class OrganizationsController {

    private final DtoConverter dtoConverter;
    private final OrganizationsService organizationsService;
    private final OrganizationsReadService organizationsReadService;
    private final UsersService usersService;
    private final UsersReadService usersReadService;
    private final RandomFieldsGenerator randomFieldsGenerator;

    @Autowired
    public OrganizationsController(DtoConverter dtoConverter,
                                   OrganizationsService organizationsService,
                                   OrganizationsReadService organizationsReadService,
                                   UsersService usersService,
                                   UsersReadService usersReadService,
                                   RandomFieldsGenerator randomFieldsGenerator) {
        this.dtoConverter = dtoConverter;
        this.organizationsService = organizationsService;
        this.organizationsReadService = organizationsReadService;
        this.usersService = usersService;
        this.usersReadService = usersReadService;
        this.randomFieldsGenerator = randomFieldsGenerator;
    }

    @GetMapping("/{organizationId}")
    @ResponseStatus(OK)
    @ApiOperation("Get information for an organization")
    @AdminOrUserOfTargetOrganization
    public OrganizationResponse getOrganization(User requestingUser, @PathVariable UUID organizationId) {
        return dtoConverter.convert(organizationsReadService.getById(organizationId));
    }

    @PutMapping("/{organizationId}")
    @ResponseStatus(OK)
    @ApiOperation("Update Organization")
    @AdminOrAdminOfTargetOrganization
    public void updateOrganization(User requestingUser,
                                   @PathVariable UUID organizationId,
                                   @RequestBody @Valid UpdateOrganizationRequest request) {
        organizationsService.updateOrganization(requestingUser.getId(), organizationId,
                request.getOrganizationName(), request.getStreet(), request.getCity(), request.getState(), request.getCountry(),
                request.getPostalCode(), request.getWebsiteUrl(), request.getContactName(), request.getPhoneNumber(),
                request.getEmailAddress());
    }

    @GetMapping("/{organizationId}/users")
    @ApiOperation("Retrieve a list of users for an organization")
    @AdminOrUserOfTargetOrganization
    public List<UserResponse> listOrganizationUsers(User requestingUser, @PathVariable UUID organizationId) {
        return usersReadService.getUsersForOrganization(organizationId).stream()
                .map(dtoConverter::convert)
                .collect(toList());
    }

    @PostMapping("/{organizationId}/users")
    @ApiOperation("Create a new user for an organization")
    @ResponseStatus(CREATED)
    @AdminOrAdminOfTargetOrganization
    public UUID createUser(User requestingUser, @PathVariable UUID organizationId, @RequestBody @Valid NewUserRequest request) {
        return usersService.createUser(requestingUser.getId(),
                organizationId, request.getUsername(), request.getDisplayName(), request.getPassword());
    }
}
