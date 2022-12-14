package dev.tolstov.buspark.dto;


import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.model.DriverLicense;
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

    public EmployeeDriverDTO() {
    }

    public EmployeeDriverDTO(
            String name,
            String lastName,
            Double salary,
            Address homeAddress,
            String post,
            DriverLicense driverLicense
    ) {
        super(name, lastName, salary, homeAddress);
        this.post = post;
        this.driverLicense = driverLicense;
    }

    // по хорошему у механика тоже могут быть водительские права
}
