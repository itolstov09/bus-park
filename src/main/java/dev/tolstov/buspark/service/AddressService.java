package dev.tolstov.buspark.service;

import dev.tolstov.buspark.model.Address;

import javax.validation.constraints.NotBlank;
import java.util.List;

public interface AddressService extends BusParkBaseService<Address> {

    Address save(Address address);
    Address update(Address address);
    List<Address> saveAll(Iterable<Address> addresses);
//    List<Address> findAddresesWithoutApartmentNumber();
    Integer updateStreetAddress(@NotBlank String street, Long id);
    List<Address> findBusStopAddresses();
}
