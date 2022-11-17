package dev.tolstov.buspark.service;

import dev.tolstov.buspark.exception.BPEntityNotFoundException;
import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@Service
public class AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    Validator validator;


    public Address save(Address newAddress) {
        addressValidation(newAddress);
        return addressRepository.save(newAddress);
    }

    private void addressValidation(Address newAddress) {
        Set<ConstraintViolation<Address>> constraintViolations = validator.validate(newAddress);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
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
