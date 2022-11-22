package dev.tolstov.buspark.controller;

import dev.tolstov.buspark.exception.EmployeeException;
import dev.tolstov.buspark.model.*;
import dev.tolstov.buspark.service.EmployeeService;
import dev.tolstov.buspark.validation.use_cases.OnEmployeeAddressSave;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("api/v1/employees")
@Validated
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
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(OnEmployeeAddressSave.class)
    public Employee saveDriver(@RequestBody @Valid EmployeeDriverDTO dto) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(dto, employee);
        Employee.Post post = Employee.Post.valueOf(dto.getPost());
        employee.setPost(post);
        return employeeService.save(employee, dto.getHomeAddress(), dto.getDriverLicense());
    }

    @PostMapping("/mechanic")
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(OnEmployeeAddressSave.class)
    public Employee saveMechanic( @RequestBody @Valid EmployeeMechanicDTO dto ) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(dto, employee);
        Employee.Post post = Employee.Post.valueOf(dto.getPost());
        employee.setPost(post);
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

