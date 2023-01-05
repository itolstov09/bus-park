package dev.tolstov.buspark.service;

import dev.tolstov.buspark.exception.BPEntityNotFoundException;
import dev.tolstov.buspark.exception.BusStopException;
import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.model.BusStop;
import dev.tolstov.buspark.repository.BusStopRepository;
import dev.tolstov.buspark.validation.ValidationService;
import dev.tolstov.buspark.validation.ValidationUseCaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityExistsException;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Objects;

@Service
@Validated
public class BusStopServiceImpl implements BusStopService {

    private final BusStopRepository busStopRepository;

    private final AddressServiceImpl addressServiceImpl;

    private final ValidationService validationService;

    private final ValidationUseCaseService validationUseCaseService;


    @Autowired
    public BusStopServiceImpl(
            BusStopRepository busStopRepository,
            AddressServiceImpl addressServiceImpl,
            ValidationService validationService,
            ValidationUseCaseService validationUseCaseService
    ) {
        this.busStopRepository = busStopRepository;
        this.addressServiceImpl = addressServiceImpl;
        this.validationService = validationService;
        this.validationUseCaseService = validationUseCaseService;
    }


    @Override
    public BusStop save(BusStop busStop) {
        validationService.busStopValidation(busStop);
        //если создание то не должно быть записи с таким же уникальным значением
//        id - null, нет записи со значением
        //если обновление то не должно быть записи с таким же уникальным значением
//        id содержит значение, id записи равно id по уникальному значению


        // Ниже попытка уложить в один if этой констролябии
//        if (busStopId == null) {
//            if (busStopRepository.existsByName(busStopName)) {
//                throw new EntityExistsException(
//                        String.format("Bus stop with name \"%s\" exists!", busStopName)
//                );
//            }
//        } else {
//            Long idByName = busStopRepository.getIdByName(busStopName);
//            if (idByName != null && !Objects.equals(idByName, busStopId)) {
//                throw new EntityExistsException(
//                        String.format("Bus stop with name \"%s\" exists!", busStopName)
//                );
//            }
//        }
        checkUniqueValues(busStop);

        addressServiceImpl.save(busStop.getAddress());
        return busStopRepository.save(busStop);
    }

    private void checkUniqueValues(BusStop busStop) {
        Long busStopId = busStop.getId();

        String busStopName = busStop.getName();
        if (existByName(busStopName, busStopId)) {
            throw new EntityExistsException(
                    String.format("Bus stop with name \"%s\" exists!", busStopName)
            );
        }

    }

    private boolean existByName(String busStopName, Long busStopId) {
        Long idByName = busStopRepository.getIdByName(busStopName);
        return ( busStopId == null && busStopRepository.existsByName(busStopName)
                || idByName != null && !Objects.equals(idByName, busStopId) );
    }

    @Override
    public List<BusStop> findAll() {
        return busStopRepository.findAll();
    }

    @Override
    public BusStop findById(Long busStopId) {
        return busStopRepository.findById(busStopId)
                .orElseThrow(
                        () -> new BPEntityNotFoundException( BusStop.class.getSimpleName(), busStopId)
                );
    }

    @Override
    public BusStop update(Long id, BusStop busStopInfo) {
        BusStop busStop = findById(id);
        Long addressId = busStopInfo.getAddress().getId();
        if (addressId != null && addressServiceImpl.findById(addressId).getApartmentNumber() != null) {
            throw new BusStopException(
                    String.format("Cannot update bus stop address: address id '%s' is employee address", addressId)
            );
        }
        BeanUtils.copyProperties(busStopInfo, busStop);
        return save(busStop);
    }

    @Override
    public void deleteById(Long busStopId) {
        BusStop byId = findById(busStopId);
        busStopRepository.delete(byId);
    }

    @Override
    public void deleteAll() {
        busStopRepository.deleteAll();
    }

    @Override
    public Page<BusStop> getPage(@Min(0) Integer page, @Positive Integer size) {
        return busStopRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public BusStop findByName(@NotBlank String name) {
        return busStopRepository.findOneByName(name).orElseThrow(
                () -> new BPEntityNotFoundException(BusStop.class.getSimpleName(), name)
        );
    }

    @Override
    public List<BusStop> findByStreet(@NotBlank String street) {
        return busStopRepository.findByAddressStreet(street);
    }

    @Transactional
    public Integer editName(@NotBlank String name, @Positive Long id) {
        if (existByName(name, id)) {
            throw new EntityExistsException(
                    String.format("Cannot set bus stop name: bus stop with name '%s' exists", name) );
        }
        return busStopRepository.editName(name, id);
    }

    @Transactional
    public Integer setAddress(@Positive Long addressId, @Positive Long busStopId) {
        Address address = addressServiceImpl.findById(addressId);
        validationUseCaseService.busStopAddressValidation(address);

        return busStopRepository.setAddress(addressId, busStopId);
    }

    @Transactional
    @Override
    public Integer updateAddressStreet(@NotBlank String street, Long id) {
        if (!busStopRepository.existsById(id)) {
            throw new BPEntityNotFoundException(BusStop.class.getSimpleName(), id);
        }
        Long addressId = busStopRepository.getAddressIdById(id);
        return addressServiceImpl.updateStreetAddress(street, addressId);
    }

    @Override
    public boolean existById(Long busStopId) {
        return busStopRepository.existsById(busStopId);
    }

    //todo пока нет общей картины. так что ну думаю что стоит использовать удаление
//    @Transactional
//    public void deleteByName(@NotBlank String name) {
//        if (!busStopRepository.existsByName(name)) {
//            throw new BPEntityNotFoundException(String.format("Cannot delete entity with name '%s'. Not found.", name));
//        }
//        busStopRepository.deleteByName(name);
//    }
}