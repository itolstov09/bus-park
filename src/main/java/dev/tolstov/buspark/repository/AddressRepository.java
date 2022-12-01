package dev.tolstov.buspark.repository;

import dev.tolstov.buspark.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AddressRepository extends JpaRepository<Address, Long> {
    @Modifying
    @Query("UPDATE Address address " +
            "SET address.street=?1 " +
            "WHERE address.id=?2"
    )
    Integer updateStreetAddress(String street, Long addressId);
}
