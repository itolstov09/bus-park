package dev.tolstov.buspark.service;

import dev.tolstov.buspark.exception.BPEntityNotFoundException;
import dev.tolstov.buspark.model.Bus;
import dev.tolstov.buspark.model.Employee;
import dev.tolstov.buspark.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class BusService {
    @Autowired
    BusRepository busRepository;


    public Bus save(Bus newBus) {
        return busRepository.save(newBus);
    }

    public Bus save(Bus newBus, Employee driver) {
        newBus.setDriver(driver);
        return busRepository.save(newBus);
    }

    public Bus save(Bus newBus, Employee driver, Set<Employee> mechanics) {
        newBus.setDriver(driver);
        newBus.setMechanics(mechanics);
        return busRepository.save(newBus);
    }


    public List<Bus> findAll() {
        return busRepository.findAll();
    }

    public Bus findById(Long busId) {
        return busRepository.findById(busId)
                .orElseThrow(
                        () -> new BPEntityNotFoundException(String.format("Bus with id \"%s\" not found!", busId))
                );
    }

    public Bus update(Bus busInfo) {
        // TODO возможно стоит сделать проверку id != null, ведь если это так, то это не обновление, а создание
        return busRepository.save(busInfo);
    }

    public void deleteById(Long busId) {
        busRepository.deleteById(busId);
    }

    public void deleteAll() {
        busRepository.deleteAll();
    }

    public List<Bus> findBusesByMechanicId(Long mechanicId) {
        return busRepository.findBusesByMechanicId(mechanicId);
    }
}
