package dev.tolstov.buspark.model;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String lastName;

    private String middleName;

    @NonNull
    private Double salary;

    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "home_address_ID",
        foreignKey = @ForeignKey(name = "employee_home_address_ID_fkey"))
    private Address homeAddress;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Post post;

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }


    public enum Post {
        DRIVER,
        MECHANIC
    }
}


