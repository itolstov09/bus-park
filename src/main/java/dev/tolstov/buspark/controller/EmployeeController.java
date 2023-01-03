package dev.tolstov.buspark.controller;

import dev.tolstov.buspark.dto.EmployeeDriverDTO;
import dev.tolstov.buspark.dto.EmployeeFIODTO;
import dev.tolstov.buspark.dto.EmployeeMechanicDTO;
import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.model.DriverLicense;
import dev.tolstov.buspark.model.Employee;
import dev.tolstov.buspark.service.EmployeeServiceImpl;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/employees")
public class EmployeeController {

    @Autowired
    EmployeeServiceImpl employeeServiceImpl;


    @GetMapping
    public ResponseEntity<Page<Employee>> getPage(
            @RequestParam Integer page,
            @RequestParam Integer size
    ) {
        return ResponseEntity.ok(employeeServiceImpl.getPage(page, size));
    }

    @GetMapping("find-byLastName")
    public ResponseEntity<List<Employee>> findByLastName(@RequestParam String lastName) {
        List<Employee> employeeList = employeeServiceImpl.findByLastName(lastName);
        if (employeeList == null || employeeList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(employeeList);
    }

    @GetMapping("find-byName")
    public ResponseEntity<List<Employee>> findByName(@RequestParam String name) {
        List<Employee> employeeList = employeeServiceImpl.findByName(name);
        if (employeeList == null || employeeList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(employeeList);
    }

    @GetMapping("find-byMiddleName")
    public ResponseEntity<List<Employee>> findByMiddleName(@RequestParam String middleName) {
        List<Employee> employeeList = employeeServiceImpl.findByMiddleName(middleName);
        if (employeeList == null || employeeList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(employeeList);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Employee> findById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeServiceImpl.findById(id));
    }

    @PostMapping("/driver")
    public ResponseEntity<Employee> saveDriver(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = {
                            @ExampleObject(
                                    name = "valid",
                                    ref = "#/components/examples/employee-driver-POST-201"),
                            @ExampleObject(
                                    name = "invalid",
                                    ref = "#/components/examples/employee-driver-POST-400")
                    }
                    ) )
            @RequestBody EmployeeDriverDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeServiceImpl.createDriver(dto));
    }

    @PostMapping("/mechanic")
    public ResponseEntity<Employee> saveMechanic(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = {
                            @ExampleObject(
                                    name = "valid 1",
                                    ref = "#/components/examples/employee-mechanic-POST-201-ex1"),
                            @ExampleObject(
                                    name = "valid 2",
                                    ref = "#/components/examples/employee-mechanic-POST-201-ex2"),
                            @ExampleObject(
                                    name = "invalid",
                                    ref = "#/components/examples/employee-mechanic-POST-400")
                    }
                    ) )
            @RequestBody EmployeeMechanicDTO mechanic ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeServiceImpl.createMechanic(mechanic));
    }

    @PutMapping("/driver/{id}")
    public ResponseEntity<Employee> updateDriver(
            @PathVariable Long id,
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = {
                            @ExampleObject(
                                    name = "valid",
                                    ref = "#/components/examples/employee-driver-PUT-200"),
                            @ExampleObject(
                                    name = "invalid",
                                    ref = "#/components/examples/employee-driver-PUT-400")
                    }
                    ) )
                    EmployeeDriverDTO dto) {
        return ResponseEntity.ok(employeeServiceImpl.updateDriver(id, dto));
    }

    @PutMapping("/mechanic/{id}")
    public ResponseEntity<Employee> updateMechanic(@PathVariable Long id, @RequestBody EmployeeMechanicDTO dto) {
        return ResponseEntity.ok(employeeServiceImpl.updateMechanic(id, dto));
    }

    @PatchMapping("/{id}/editFIO")
    public ResponseEntity<Void> updateFIO(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = {
                            @ExampleObject(
                                    name = "valid 1",
                                    ref = "#/components/examples/employee-PATCH-editFIO-204-ex1"),
                            @ExampleObject(
                                    name = "valid 2",
                                    ref = "#/components/examples/employee-PATCH-editFIO-204-ex2")
                    } ) )
            @RequestBody EmployeeFIODTO dto) {
        employeeServiceImpl.updateFIO(dto, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/editHomeAddress")
    public ResponseEntity<Void> editHomeAddress(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = {
                            @ExampleObject(
                                    name = "valid",
                                    ref = "#/components/examples/employee-PATCH-editHomeAddress-204") } ) )
            @RequestBody Address homeAddress
    ) {
        employeeServiceImpl.editHomeAddress(homeAddress, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/editDriveLicense")
    public ResponseEntity<String> editLicense(
            @PathVariable Long id,
            @RequestBody DriverLicense license
    ) {
        return ResponseEntity.ok(String.format("Rows updated: %d", employeeServiceImpl.editLicense(license, id)));
    }

    @PatchMapping("/{id}/editPost")
    public ResponseEntity<Void> editPost(@PathVariable Long id, @RequestParam String newPost ) {
        employeeServiceImpl.editPost(newPost, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        employeeServiceImpl.deleteById(id);
        return ResponseEntity.noContent().build();
    }




}

