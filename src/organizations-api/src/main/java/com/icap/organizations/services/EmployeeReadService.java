package com.icap.organizations.services;

import com.icap.axon.common.services.ReadService;

import java.util.List;
import java.util.UUID;

public interface EmployeeReadService extends ReadService<Employee> {

    List<Employee> getEmployees();

    boolean exists(UUID employeeId);
}
