package dev.tolstov.buspark.service;

import dev.tolstov.buspark.model.BusStop;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;

public interface BusStopService extends BusParkBaseService<BusStop> {
      BusStop save(BusStop busStop);
      BusStop update(Long id, BusStop busStop);
      BusStop findByName(@NotBlank String name);
      List<BusStop> findByStreet(@NotBlank String street);
      Integer editName(@NotBlank String name, @Positive Long id);
      Integer setAddress(@Positive Long addressId, @Positive Long busStopId);
      Integer updateAddressStreet(@NotBlank String street, Long busStopId);
      boolean existById(Long id);
}
