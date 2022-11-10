package dev.tolstov.buspark.model;

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

    @Getter @Setter
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

    public boolean isCanBeDriver() {
        return driverLicense != null;
    }


    public enum Post {
        DRIVER,
        MECHANIC
    }
}


