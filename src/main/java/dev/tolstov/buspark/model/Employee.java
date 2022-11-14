package dev.tolstov.buspark.model;

import dev.tolstov.buspark.exception.EmployeeException;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class Employee {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Getter @Setter
    private String name;

    @NonNull
    @Getter @Setter
    private String lastName;

    @Getter @Setter
    private String middleName;

    @NonNull
    @Getter @Setter
    private Double salary;

    @Getter
    @ManyToOne(optional = false)
    @JoinColumn(name = "home_address_ID",
        foreignKey = @ForeignKey(name = "employee_home_address_ID_fkey"))
    private Address homeAddress;

    @NonNull
    @Getter @Setter
    @Enumerated(EnumType.STRING)
    private Post post;

    @Embedded
    @Getter
    @Setter
    private DriverLicense driverLicense;

    public void setHomeAddress(Address homeAddress) {
        homeAddressCheck(homeAddress);
        this.homeAddress = homeAddress;
    }

    private void homeAddressCheck(Address homeAddress) {
        if (homeAddress.getApartmentNumber() == null) {
            throw new EmployeeException("Employee home address must have apartment number!");
        }
    }

    public boolean isCanBeDriver() {
        return driverLicense != null;
    }


    public enum Post {
        DRIVER,
        MECHANIC
    }
}


