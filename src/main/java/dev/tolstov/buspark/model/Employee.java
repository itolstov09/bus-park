package dev.tolstov.buspark.model;

import dev.tolstov.buspark.validation.constraints.PostSubset;
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
public class Employee {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NonNull
    @Getter @Setter
    private String name;

    @NotBlank
    @NonNull
    @Getter @Setter
    private String lastName;

    @Getter @Setter
    private String middleName;

    @Positive
    @NonNull
    @Getter @Setter
    private Double salary;

    @Valid
    @NotNull
    @Getter
    @ManyToOne(optional = false)
    @JoinColumn(name = "home_address_ID",
        foreignKey = @ForeignKey(name = "employee_home_address_ID_fkey"))
    private Address homeAddress;

    @NotNull
    @PostSubset(anyOf = {Post.DRIVER, Post.MECHANIC})
    @NonNull @Getter @Setter
    @Enumerated(EnumType.STRING)
    private Post post;

    @Getter @Setter
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


