package dev.tolstov.buspark.controller;

import dev.tolstov.buspark.exception.EmployeeException;
import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.model.DriverLicense;
import dev.tolstov.buspark.model.Employee;
import dev.tolstov.buspark.model.EmployeeMechanicDTO;
import dev.tolstov.buspark.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;


    @GetMapping
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

    @GetMapping("/{id}")
    public Employee findById(@PathVariable Long id) {
        return employeeService.findById(id);
    }

    @PostMapping("/driver")
    public Employee saveDriver(@RequestBody Employee employee) {
        Address homeAddress = employee.getHomeAddress();
        DriverLicense driverLicense = employee.getDriverLicense();
        return employeeService.save(employee, homeAddress, driverLicense);
    }

    @PostMapping("/mechanic")
    public Employee saveMechanic(@RequestBody EmployeeMechanicDTO dto) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(dto, employee);
        try {
            Employee.Post post = Employee.Post.valueOf(dto.getPost());
            // TODO setPost тоже может вызвать исключение. что не есть хорошо
            employee.setPost(post);
        } catch (IllegalArgumentException exception) {
            throw new EmployeeException("Invalid employee post: "+ dto.getPost());
        }
        Address homeAddress = dto.getHomeAddress();
        return employeeService.save(employee, homeAddress);
    }

    @PutMapping("/{id}")
    public Employee update(@PathVariable Long id, @RequestBody Employee employee) {
        return employeeService.update(id, employee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        employeeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }




}

