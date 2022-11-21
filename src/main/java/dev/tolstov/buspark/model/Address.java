package dev.tolstov.buspark.model;

import dev.tolstov.buspark.validation.use_cases.OnBusStopAddressSave;
import dev.tolstov.buspark.validation.use_cases.OnEmployeeAddressSave;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Entity
@NoArgsConstructor
public class Address {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Setter @Getter
    @NotBlank
    @Column(nullable = false)
    private String street;

    @NotNull(groups = OnEmployeeAddressSave.class, message = "Employee address must have apartment number")
    @Null(groups = OnBusStopAddressSave.class, message = "Bus stop address apartment number must be null")
    @Min(1)
    @Setter @Getter
    @Column(name = "apartment_number")
    private Integer apartmentNumber;

    public Address(@NonNull String street, Integer apartmentNumber) {
        this.street = street;
        this.apartmentNumber = apartmentNumber;
    }
}