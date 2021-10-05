package com.icap.organizations.services;

import com.icap.organizations.persistence.EmployeeRepository;
import com.icap.organizations.persistence.PersistableEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
public class DefaultEmployeeReadService implements EmployeeReadService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public DefaultEmployeeReadService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public boolean exists(UUID employeeId) {
        return employeeRepository.findById(employeeId).isPresent();
    }

    @Override
    public Employee getById(UUID id) {
        return convert(employeeRepository.findById(id).orElseThrow());
    }

    @Override
    public List<Employee> getEmployees() {
        return employeeRepository.findAll().stream()
                .map(DefaultEmployeeReadService::convert)
                .collect(toList());
    }

    private static Employee convert(PersistableEmployee persistableEmployee) {
        return new Employee(persistableEmployee.getId(), persistableEmployee.getUserId(), persistableEmployee.getFirstName(),
                persistableEmployee.getLastName(), persistableEmployee.getDateOfBirth(), persistableEmployee.getJobTitle(),
                persistableEmployee.getEmployeeNumber(), persistableEmployee.getImmediateSupervisorId(),
                persistableEmployee.getRegisteredOn(), persistableEmployee.isDisabled());
    }

}
