package dev.tolstov.buspark.model;


import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class EmployeeDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String lastName;
    private String middleName;
    @Positive
    private Double salary;
    @Valid
    @NotNull
    private Address homeAddress;
}
