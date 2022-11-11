package dev.tolstov.buspark.service;

import dev.tolstov.buspark.exception.BPEntityNotFoundException;
import dev.tolstov.buspark.exception.EmployeeException;
import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.model.DriverLicense;
import dev.tolstov.buspark.model.Employee;
import dev.tolstov.buspark.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;


    public Employee save(Employee newEmployee, Address homeAddress) {
        //TODO если приходит водитель, то нельзя его сохранять, если у него нет прав
        newEmployee.setHomeAddress(homeAddress);
        return employeeRepository.save(newEmployee);
    }

    public Employee save(Employee newEmployee, Address homeAddress, DriverLicense license) {
        if (license == null) {
            throw new EmployeeException("DriverLicense must be not null");
        }
        newEmployee.setHomeAddress(homeAddress);
        newEmployee.setDriverLicense(license);
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

    public void deleteAll() {
        employeeRepository.deleteAll();
    }

    public Employee findFirstByPost(Employee.Post post) {
        return employeeRepository.findFirstByPost(post);
    }
}

