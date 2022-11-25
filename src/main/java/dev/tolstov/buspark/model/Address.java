package dev.tolstov.buspark.model;

import dev.tolstov.buspark.validation.use_cases.OnBusStopAddressSave;
import dev.tolstov.buspark.validation.use_cases.OnEmployeeAddressSave;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Address {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotBlank
    @Column(nullable = false)
    private String street;

    @NotNull(groups = OnEmployeeAddressSave.class, message = "Employee address must have apartment number")
    @Null(groups = OnBusStopAddressSave.class, message = "Bus stop address apartment number must be null")
    @Min(1)
    @Column(name = "apartment_number")
    private Integer apartmentNumber;

    public Address(@NonNull String street, Integer apartmentNumber) {
        this.street = street;
        this.apartmentNumber = apartmentNumber;
    }
}