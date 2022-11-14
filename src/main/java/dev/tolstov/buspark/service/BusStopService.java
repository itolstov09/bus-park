package dev.tolstov.buspark.service;

import dev.tolstov.buspark.exception.BPEntityNotFoundException;
import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.model.BusStop;
import dev.tolstov.buspark.repository.BusStopRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;

@Service
public class BusStopService {

    @Autowired
    BusStopRepository busStopRepository;

    @Autowired
    AddressService addressService;


    public BusStop save(BusStop newBusStop, Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Bus stop address must be not null");
        }
        if (busStopRepository.existsByName(newBusStop.getName())) {
            throw new EntityExistsException(String.format("Bus stop with name \"%s\" exists!", newBusStop.getName()));
        }
        addressService.save(address);
        newBusStop.setAddress(address);
        return busStopRepository.save(newBusStop);
    }

    public List<BusStop> findAll() {
        return busStopRepository.findAll();
    }

    public BusStop findById(Long busStopId) {
        return busStopRepository.findById(busStopId)
                .orElseThrow(
                        () -> new BPEntityNotFoundException( BusStop.class.getSimpleName(), busStopId)
                );
    }

    public BusStop update(Long id, BusStop busStopInfo) {
        BusStop busStop = findById(id);
        BeanUtils.copyProperties(busStopInfo, busStop);
        return busStopRepository.save(busStop);
    }

    public void deleteById(Long busStopId) {
        BusStop byId = findById(busStopId);
        // удаляем зависимость перед удалением основы
//        Long addressId = byId.getAddress().getId();
//        addressService.deleteById(addressId);
        busStopRepository.delete(byId);
    }


    public void deleteAll() {
        busStopRepository.deleteAll();
    }
}
