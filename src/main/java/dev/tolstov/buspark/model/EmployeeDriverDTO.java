package dev.tolstov.buspark.model;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@EqualsAndHashCode(callSuper = true)
@Data
public class EmployeeDriverDTO extends EmployeeDTO {

    @Pattern(regexp = "DRIVER")
    private String post;

    @Valid
    @NotNull
    private DriverLicense driverLicense;

    // по хорошему у механика тоже могут быть водительские права
}
