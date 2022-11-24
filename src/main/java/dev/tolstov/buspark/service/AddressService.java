package dev.tolstov.buspark.service;

import dev.tolstov.buspark.exception.BPEntityNotFoundException;
import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.repository.AddressRepository;
import dev.tolstov.buspark.validation.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    ValidationService validationService;


    public Address save(Address newAddress) {
        validationService.addressValidation(newAddress);
        return addressRepository.save(newAddress);
    }


    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    public Address findById(Long addressId) {
        return addressRepository.findById(addressId).orElseThrow(
                () -> new BPEntityNotFoundException(Address.class.getSimpleName(), addressId));
    }

    // todo добавить валидацию
    public Address update(Address addressInfo) {
        return addressRepository.save(addressInfo);
    }

    public void deleteById(Long addressId) {
        addressRepository.deleteById(addressId);
    }


    public void deleteAll() {
        addressRepository.deleteAll();
    }

    public List<Address> saveAll(Iterable<Address> addressList) {
        return addressRepository.saveAll(addressList);
    }
}
