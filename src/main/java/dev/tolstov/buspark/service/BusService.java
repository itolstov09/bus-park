package dev.tolstov.buspark.service;

import dev.tolstov.buspark.dto.BusDTO;
import dev.tolstov.buspark.model.Bus;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

public interface BusService extends BusParkBaseService<Bus> {
    Bus create(@Valid BusDTO dto);

    Bus update(Long id, @Valid BusDTO dto);

    List<Bus> findBusesByMechanicId(Long mechanicId);

    void addMechanic(@Positive Long mechanicId, @Positive Long busId);

    void removeMechanic(Long mechanicId, Long busId);

    void setBusDriver(Long driverId, Long busId);

    void setRoute(Long routeId, Long busId);

}
