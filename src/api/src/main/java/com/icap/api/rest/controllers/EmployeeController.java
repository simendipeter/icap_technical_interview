package com.icap.api.rest.controllers;

import com.icap.api.rest.annotations.AdminOnly;
import com.icap.api.rest.converters.DtoConverter;
import com.icap.api.rest.requests.NewEmployeeRequest;
import com.icap.api.rest.responses.EmployeeResponse;
import com.icap.organizations.services.EmployeeReadService;
import com.icap.organizations.services.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/api/employees")
@Api(consumes = APPLICATION_JSON_VALUE, tags = "Employee")
public class EmployeeController {

    private final DtoConverter dtoConverter;
    private final EmployeeService employeeService;
    private final EmployeeReadService employeeReadService;

    @Autowired
    public EmployeeController(DtoConverter dtoConverter,
                              EmployeeService employeeService,
                              EmployeeReadService employeeReadService) {
        this.dtoConverter = dtoConverter;
        this.employeeService = employeeService;
        this.employeeReadService = employeeReadService;
    }

    @GetMapping
    @ResponseStatus(OK)
    @ApiOperation("Retrieves details of all organizations")
    @AdminOnly
    public List<EmployeeResponse> getAllEmployees() {
        return employeeReadService.getEmployees().stream()
                .map(dtoConverter::convert)
                .collect(toList());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation("Create a new employee")
    @AdminOnly
    public UUID newEmployee(@RequestBody @Valid NewEmployeeRequest request) {
        return employeeService.createRegisteredEmployee(request.getUserId(), request.getFirstName(),
                request.getLastName(), request.getDateOfBirth(), request.getJobTitle(), request.getEmployeeNumber(),
                request.getImmediateSupervisorId(), request.getRegisteredOn(), request.isDisabled());
    }

    @DeleteMapping("/{employeeId}")
    @ResponseStatus(OK)
    @ApiOperation("Disable an employee")
    @AdminOnly
    public void disableEmployee(@PathVariable UUID employeeId) {
        employeeService.disableEmployee(employeeId);
    }

    @PostMapping("/{employeeId}")
    @ResponseStatus(OK)
    @ApiOperation("Enable an employee")
    @AdminOnly
    public void enableOrganization(@PathVariable UUID employeeId) {
        employeeService.enableEmployee(employeeId);
    }
}
