package dev.tolstov.buspark.repository;

import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.model.BusStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BusStopRepository extends JpaRepository<BusStop, Long> {
    boolean existsByName(String name);

    @Query("SELECT busStop.id " +
            "FROM BusStop busStop " +
            "WHERE busStop.name=?1")
    Long getIdByName(String name);

    Optional<BusStop> findOneByName(String name);

    List<BusStop> findByAddressStreet(String street);

    @Modifying
    @Query("UPDATE BusStop busStop " +
            "SET busStop.name=?1 " +
            "WHERE busStop.id=?2")
    Integer editName(String name, Long id);

    @Modifying
    @Query(value = "UPDATE bus_stop " +
            "SET address_id=?1 " +
            "WHERE id=?2",
            nativeQuery = true)
    Integer setAddress(Long addressId, Long busStopId);

    @Query("select busStop.address.id from BusStop busStop where busStop.id=?1")
    Long getAddressIdById(Long id);

    void deleteByName(String name);
}
