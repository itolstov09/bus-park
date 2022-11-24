package dev.tolstov.buspark.dto;


import dev.tolstov.buspark.model.Address;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {
    @NotBlank
    @NonNull
    private String name;
    @NotBlank
    @NonNull
    private String lastName;
    private String middleName;
    @Positive
    @NonNull
    private Double salary;
    @Valid
    @NotNull
    @NonNull
    private Address homeAddress;
}
