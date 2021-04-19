package com.elite.employee.management.service;

import com.elite.employee.management.entity.Employee;
import com.elite.employee.management.model.EmployeeDTO;
import com.elite.employee.management.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLDataException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public List<Employee> saveAll(List<EmployeeDTO> employeeDTOList) {
        List<Employee> employees = employeeDTOList.parallelStream().map(this::mapToEntity).collect(Collectors.toList());
        employeeRepository.saveAll(employees);
        return employees;
    }

    @Override
    public EmployeeDTO create(EmployeeDTO employeeDTO) {
        Employee employee = mapToEntity(employeeDTO);
        employeeRepository.save(employee);
        employeeDTO.setId(employee.getId());
        return employeeDTO;
    }

    private Employee mapToEntity(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setId(employeeDTO.getId());
        employee.setAge(employeeDTO.getAge());
        employee.setName(employeeDTO.getName());
        return employee;
    }

    private EmployeeDTO mapToDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setAge(employee.getAge());
        employeeDTO.setName(employee.getName());
        return employeeDTO;
    }

    @Override
    public EmployeeDTO find(String id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(Long.valueOf(id));
        return employeeOptional.map(this::mapToDTO).orElseGet(EmployeeDTO::new);
    }

    @Override
    public EmployeeDTO update(EmployeeDTO employeeDTO) throws SQLDataException {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeDTO.getId());
        if (employeeOptional.isEmpty()) {
            throw new SQLDataException("Entity Not Found");
        }
        Employee employee = mapToEntity(employeeDTO);
        employeeRepository.save(employee);
        return employeeDTO;
    }

    @Override
    public String delete(String id) {
        employeeRepository.deleteById(Long.valueOf(id));
        return id;
    }
}
