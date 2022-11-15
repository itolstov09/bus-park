package dev.tolstov.buspark.service;

import dev.tolstov.buspark.exception.BPEntityNotFoundException;
import dev.tolstov.buspark.exception.EmployeeException;
import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.model.DriverLicense;
import dev.tolstov.buspark.model.Employee;
import dev.tolstov.buspark.repository.EmployeeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    AddressService addressService;


    public Employee save(Employee newEmployee, Address homeAddress) {
        //TODO если приходит водитель, то нельзя его сохранять, если у него нет прав
        addressService.save(homeAddress);
        newEmployee.setHomeAddress(homeAddress);
        return employeeRepository.save(newEmployee);
    }

    public Employee save(Employee newEmployee, Address homeAddress, DriverLicense license) {
        if (license == null) {
            throw new EmployeeException("DriverLicense must be not null");
        } else if (employeeRepository.existsByDriverLicenseLicenseID(license.getLicenseID())) {
            throw new EmployeeException("Cannot save driver with exist driver license");
        }

        addressService.save(homeAddress);
        newEmployee.setHomeAddress(homeAddress);
        newEmployee.setDriverLicense(license);
        return employeeRepository.save(newEmployee);
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee findById(Long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(
                () -> new BPEntityNotFoundException(
                        Employee.class.getSimpleName(), employeeId)
        );
    }

    public Employee update(Long id, Employee employeeInfo) {
        Employee byId = findById(id);
        BeanUtils.copyProperties(employeeInfo, byId);
        return employeeRepository.save(byId);
    }

    public void deleteById(Long employeeId) {
        Employee byId = findById(employeeId);
        employeeRepository.delete(byId);
    }

    public void deleteAll() {
        employeeRepository.deleteAll();
    }

    public Employee findFirstByPost(Employee.Post post) {
        return employeeRepository.findFirstByPost(post);
    }
}

