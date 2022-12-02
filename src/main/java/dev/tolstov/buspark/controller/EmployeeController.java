package dev.tolstov.buspark.controller;

import dev.tolstov.buspark.dto.EmployeeDriverDTO;
import dev.tolstov.buspark.dto.EmployeeFIODTO;
import dev.tolstov.buspark.dto.EmployeeMechanicDTO;
import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.model.DriverLicense;
import dev.tolstov.buspark.model.Employee;
import dev.tolstov.buspark.service.EmployeeService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("api/v1/employees")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;


    @GetMapping
    public ResponseEntity<Page<Employee>> getPage(
            @RequestParam Integer page,
            @RequestParam Integer size
    ) {
        return ResponseEntity.ok(employeeService.getPage(page, size));
    }

    @GetMapping("find-byLastName")
    public ResponseEntity<List<Employee>> findByLastName(@RequestParam String lastName) {
        List<Employee> employeeList = employeeService.findByLastName(lastName);
        if (employeeList == null || employeeList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(employeeList);
    }

    @GetMapping("find-byName")
    public ResponseEntity<List<Employee>> findByName(@RequestParam String name) {
        List<Employee> employeeList = employeeService.findByName(name);
        if (employeeList == null || employeeList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(employeeList);
    }

    @GetMapping("find-byMiddleName")
    public ResponseEntity<List<Employee>> findByMiddleName(@RequestParam String middleName) {
        List<Employee> employeeList = employeeService.findByMiddleName(middleName);
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
        return ResponseEntity.ok(employeeService.updateDriver(id, dto));
    }

    @PutMapping("/mechanic/{id}")
    public ResponseEntity<Employee> updateMechanic(@PathVariable Long id, @RequestBody EmployeeMechanicDTO dto) {
        return ResponseEntity.ok(employeeService.updateMechanic(id, dto));
    }

    @PatchMapping("/{id}/editFIO")
    public ResponseEntity<Void> updateFIO(
            @PathVariable Long id,
            @RequestBody EmployeeFIODTO dto) {
        employeeService.updateFIO(dto, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/editHomeAddress")
    public ResponseEntity<Void> editHomeAddress(
            @PathVariable Long id,
            @RequestBody Address homeAddress
    ) {
        employeeService.editHomeAddress(homeAddress, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/editDriveLicense")
    public ResponseEntity<String> editLicense(
            @PathVariable Long id,
            @RequestBody DriverLicense license
    ) {
        return ResponseEntity.ok(String.format("Rows updated: %d", employeeService.editLicense(license, id)));
    }

    @PatchMapping("/{id}/editPost")
    public ResponseEntity<Void> editPost(@PathVariable Long id, @RequestParam String newPost ) {
        employeeService.editPost(newPost, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        employeeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }




}

