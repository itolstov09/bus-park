package dev.tolstov.buspark.dto;


import dev.tolstov.buspark.dto.EmployeeDTO;
import dev.tolstov.buspark.model.DriverLicense;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@EqualsAndHashCode(callSuper = true)
@Data
public class EmployeeMechanicDTO extends EmployeeDTO {

    @Pattern(regexp = "MECHANIC")
    private String post;

    // по хорошему у механика тоже могут быть водительские права
    @Valid
    private DriverLicense driverLicense;
}
