package com.elite.employee.management.service;

import com.elite.employee.management.entity.Employee;
import com.elite.employee.management.model.EmployeeDTO;

import java.sql.SQLDataException;
import java.util.List;

public interface EmployeeService {
    List<Employee> saveAll(List<EmployeeDTO> employeeDTOList);

    EmployeeDTO create(EmployeeDTO employeeDTO);

    EmployeeDTO find(String id);

    EmployeeDTO update(EmployeeDTO employeeDTO) throws SQLDataException;

    String delete(String id);
}
