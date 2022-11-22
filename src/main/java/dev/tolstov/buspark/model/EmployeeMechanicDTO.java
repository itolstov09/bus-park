package dev.tolstov.buspark.model;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Pattern;

@EqualsAndHashCode(callSuper = true)
@Data
public class EmployeeMechanicDTO extends EmployeeDTO {

    @Pattern(regexp = "MECHANIC")
    private String post;

    // по хорошему у механика тоже могут быть водительские права
}
