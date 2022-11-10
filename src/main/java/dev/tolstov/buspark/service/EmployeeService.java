package dev.tolstov.buspark.service;

import dev.tolstov.buspark.exception.BPEntityNotFoundException;
import dev.tolstov.buspark.model.Employee;
import dev.tolstov.buspark.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;


    public Employee save(Employee newEmployee) {
        return employeeRepository.save(newEmployee);
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee findById(Long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(
                () -> new BPEntityNotFoundException(String.format("Employee with id \"%d\" not found!", employeeId))
        );
    }

    public Employee update(Employee employeeInfo) {
        return employeeRepository.save(employeeInfo);
    }

    public void deleteById(Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }
}

