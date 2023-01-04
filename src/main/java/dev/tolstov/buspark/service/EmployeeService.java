package dev.tolstov.buspark.service;

import dev.tolstov.buspark.dto.EmployeeDriverDTO;
import dev.tolstov.buspark.dto.EmployeeFIODTO;
import dev.tolstov.buspark.dto.EmployeeMechanicDTO;
import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.model.DriverLicense;
import dev.tolstov.buspark.model.Employee;
import dev.tolstov.buspark.validation.constraints.ValueOfEnum;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;

public interface EmployeeService extends BusParkBaseService<Employee> {
    Employee createMechanic(@Valid EmployeeMechanicDTO dto);

    Employee createDriver(@Valid EmployeeDriverDTO dto);

    Employee updateDriver(Long id, @Valid EmployeeDriverDTO dto);

    Employee updateMechanic(Long id, @Valid EmployeeMechanicDTO dto);

    Employee findFirstByPost(Employee.Post post);

    List<Employee> findByLastName(@NotBlank String lastName);

    List<Employee> findByName(@NotBlank String name);

    List<Employee> findByMiddleName(String middleName);

    Integer updateFIO(@Valid EmployeeFIODTO fio, Long id);

    void editHomeAddress(Address address, Long id);

    Integer editLicense(DriverLicense license, Long id);

    boolean existById(Long id);

    void editPost(@ValueOfEnum(enumClass = Employee.Post.class) String newPost, @Positive Long id);
}
