package dev.tolstov.buspark.controller;

import dev.tolstov.buspark.dto.EmployeeDriverDTO;
import dev.tolstov.buspark.dto.EmployeeMechanicDTO;
import dev.tolstov.buspark.model.Employee;
import dev.tolstov.buspark.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/employees")
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
    public Employee saveDriver(@RequestBody EmployeeDriverDTO dto) {
        return employeeService.save(dto);
    }

    @PostMapping("/mechanic")
    @ResponseStatus(HttpStatus.CREATED)
    public Employee saveMechanic( @RequestBody EmployeeMechanicDTO mechanic ) {
        return employeeService.save(mechanic);
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

