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
    public ResponseEntity<List<Employee>> findAll() {
        List<Employee> employeeList = employeeService.findAll();
        if (employeeList == null || employeeList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(employeeList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> findById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.findById(id));
    }

    @PostMapping("/driver")
    public ResponseEntity<Employee> saveDriver(@RequestBody EmployeeDriverDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService.createDriver(dto));
    }

    @PostMapping("/mechanic")
    public ResponseEntity<Employee> saveMechanic( @RequestBody EmployeeMechanicDTO mechanic ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService.createMechanic(mechanic));
    }

    @PutMapping("/driver/{id}")
    public ResponseEntity<Employee> updateDriver(@PathVariable Long id, @RequestBody EmployeeDriverDTO dto) {
        return ResponseEntity.ok(employeeService.updateDriver(id, dto));
    }

    @PutMapping("/mechanic/{id}")
    public ResponseEntity<Employee> updateMechanic(@PathVariable Long id, @RequestBody EmployeeMechanicDTO dto) {
        return ResponseEntity.ok(employeeService.updateMechanic(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        employeeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }




}

