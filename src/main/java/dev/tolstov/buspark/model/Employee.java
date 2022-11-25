package dev.tolstov.buspark.model;

import dev.tolstov.buspark.validation.constraints.PostSubset;
import dev.tolstov.buspark.validation.use_cases.OnDriverSave;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "employee_driver_license_id_key", columnNames = {"license_id"})
})
@NoArgsConstructor
@RequiredArgsConstructor
@Getter @Setter
public class Employee {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    @ManyToOne(optional = false)
    @JoinColumn(name = "home_address_ID",
        foreignKey = @ForeignKey(name = "employee_home_address_ID_fkey"))
    private Address homeAddress;

    @NotNull
    @PostSubset(anyOf = {Post.DRIVER, Post.MECHANIC})
    @NonNull
    @Enumerated(EnumType.STRING)
    private Post post;

    @NotNull(groups = OnDriverSave.class)
    @Valid
    @Embedded
    private DriverLicense driverLicense;

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public boolean isCanBeDriver() {
        return driverLicense != null;
    }


    public enum Post {
        DRIVER,
        MECHANIC
    }
}


