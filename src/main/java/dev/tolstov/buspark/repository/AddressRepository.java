package dev.tolstov.buspark.repository;

import dev.tolstov.buspark.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
