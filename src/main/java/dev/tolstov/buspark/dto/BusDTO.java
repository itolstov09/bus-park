package dev.tolstov.buspark.dto;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class BusDTO {

    @NotBlank
    private String model;

    @NotNull
    @Pattern(regexp = "[0-9]{3} [A-Z]{2,3} [0-9]{2}", message = "Номер не соответствует формату РК")
    private String numberPlate;

    @NotNull
    @Min(20)
    @Max(60)
    private Integer maxPassenger;
}
