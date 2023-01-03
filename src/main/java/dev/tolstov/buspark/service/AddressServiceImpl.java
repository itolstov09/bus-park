package dev.tolstov.buspark.service;

import dev.tolstov.buspark.exception.BPEntityNotFoundException;
import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.repository.AddressRepository;
import dev.tolstov.buspark.validation.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    ValidationService validationService;


    public Address save(Address newAddress) {
        validationService.addressValidation(newAddress);
        return addressRepository.save(newAddress);
    }


    @Override
    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    @Override
    public Address findById(Long addressId) {
        return addressRepository.findById(addressId).orElseThrow(
                () -> new BPEntityNotFoundException(Address.class.getSimpleName(), addressId));
    }

    public Address update(Address addressInfo) {
        return save(addressInfo);
    }

    @Override
    public void deleteById(Long addressId) {
        addressRepository.deleteById(addressId);
    }

    @Override
    public void deleteAll() {
        addressRepository.deleteAll();
    }

    public List<Address> saveAll(Iterable<Address> addressList) {
        return addressRepository.saveAll(addressList);
    }

//   ====================================================================================================
    public List<Address> findAddresesWithoutApartmentNumber() {
        return null;
    }

    @Override
    public Page<Address> getPage(Integer page, @Positive Integer size) {
        return addressRepository.findAll(PageRequest.of(page, size));
    }

    @Transactional
    public Integer updateStreetAddress(@NotBlank String street, Long addressId) {
        return addressRepository.updateStreetAddress(street, addressId);
    }

    public List<Address> findBusAddresses() {
        return addressRepository.findByApartmentNumberIsNull();
    }
}
