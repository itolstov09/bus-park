package dev.tolstov.buspark.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Address {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Setter @Getter
    @Column(nullable = false)
    private String street;

    @Setter @Getter
    @Column(name = "apartment_number")
    private Integer apartmentNumber;

    public Address(@NonNull String street, Integer apartmentNumber) {
        this.street = street;
        this.apartmentNumber = apartmentNumber;
    }
}