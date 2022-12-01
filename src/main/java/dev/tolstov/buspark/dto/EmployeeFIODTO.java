package dev.tolstov.buspark.dto;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
public class EmployeeFIODTO {
    @NonNull
    @NotBlank
    private String name;

    @NonNull
    @NotBlank
    private String lastName;

    private String middleName;
}
