package dev.tolstov.buspark.service;

import dev.tolstov.buspark.exception.BPEntityNotFoundException;
import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.model.BusStop;
import dev.tolstov.buspark.repository.BusStopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusStopService {

    @Autowired
    BusStopRepository busStopRepository;


    public BusStop save(BusStop newBusStop, Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Bus stop address must be not null");
        }
        newBusStop.setAddress(address);
        return busStopRepository.save(newBusStop);
    }

    public List<BusStop> findAll() {
        return busStopRepository.findAll();
    }

    public BusStop findById(Long busStopId) {
        return busStopRepository.findById(busStopId)
                .orElseThrow(
                        () -> new BPEntityNotFoundException(String.format("Bus stop with id \"%s\" not found!", busStopId))
                );
    }

    public BusStop update(BusStop busStopInfo) {
        return busStopRepository.save(busStopInfo);
    }

    public void deleteById(Long busStopId) {
        busStopRepository.deleteById(busStopId);
    }


    public void deleteAll() {
        busStopRepository.deleteAll();
    }
}
