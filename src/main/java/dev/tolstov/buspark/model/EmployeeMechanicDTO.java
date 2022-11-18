package dev.tolstov.buspark.model;


import dev.tolstov.buspark.validation.constraints.ValueOfEnum;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class EmployeeMechanicDTO {
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

    @NotNull
    @ValueOfEnum(
            enumClass = Employee.Post.class,
            message = "Must be one of employee post enum!")
    private String post;

    // по хорошему у механика тоже могут быть водительские права
}
