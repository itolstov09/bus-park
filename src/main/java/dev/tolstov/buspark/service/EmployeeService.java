package dev.tolstov.buspark.service;

import dev.tolstov.buspark.dto.EmployeeDriverDTO;
import dev.tolstov.buspark.dto.EmployeeMechanicDTO;
import dev.tolstov.buspark.exception.BPEntityNotFoundException;
import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.model.Employee;
import dev.tolstov.buspark.repository.EmployeeRepository;
import dev.tolstov.buspark.validation.ValidationService;
import dev.tolstov.buspark.validation.ValidationUseCaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import java.util.List;

@Service
@Validated
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    AddressService addressService;

    @Autowired
    ValidationService validationService;

    @Autowired
    ValidationUseCaseService validationUseCaseService;



    public Employee save(Employee employee, Address homeAddress) {
        employee.setHomeAddress(homeAddress);
        return save(employee);
    }

    @Validated
    public Employee save(@Valid EmployeeMechanicDTO mechanic) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(mechanic, employee);
        Employee.Post post = Employee.Post.valueOf(mechanic.getPost());
        employee.setPost(post);

        return save(employee);
    }

    @Validated
    public Employee save(@Valid EmployeeDriverDTO driver) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(driver, employee);
        Employee.Post post = Employee.Post.valueOf(driver.getPost());
        employee.setPost(post);

        validationUseCaseService.driverValidation(employee);

        if (employeeRepository.existsByDriverLicenseLicenseID(employee.getDriverLicense().getLicenseID())) {
            throw new EntityExistsException("Cannot save employee with exist driver license");
        }
        return save(employee);
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

    private Employee save(Employee employee) {
        validationService.employeeValidation(employee);

        addressService.save(employee.getHomeAddress());
        return employeeRepository.save(employee);
    }
}

