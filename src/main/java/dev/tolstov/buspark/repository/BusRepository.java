package dev.tolstov.buspark.repository;

import dev.tolstov.buspark.model.Bus;
import dev.tolstov.buspark.model.Employee;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BusRepository extends JpaRepository<Bus, Long> {
    @Query(
           value = "SELECT * " +
                   "FROM bus AS b " +
                   "JOIN bus_mechanic AS bm " +
                       "ON b.id = bm.bus_id " +
                   "WHERE bm.mechanic_id =?;",
            nativeQuery = true
    )
    List<Bus> findBusesByMechanicId(Long mechanicId);

    @Query(
            "SELECT bus.numberPlate " +
            "FROM Bus bus " +
            "JOIN Employee employee " +
                "ON bus.driver.id = employee.id " +
            "WHERE employee.name = ?1 " +
                "AND employee.lastName = ?2"
    )
    String numberPlateByDriverNameAndLastName(String name, String lastName);

    boolean existsByNumberPlate(String numberPlate);

    boolean existsByDriverId(Long driverId);

    @Query("SELECT bus.id " +
            "FROM Bus bus " +
            "WHERE bus.numberPlate=?1")
    Long getIdByNumberPlate(String numberPlate);

    @Modifying
    @Query(value = "INSERT INTO bus_mechanic VALUES (?, ?)", nativeQuery = true)
    void addMechanic(Long busId, Long mechanicId);

    @Modifying
    @Query(value = "DELETE FROM bus_mechanic " +
            "WHERE bus_id=? AND mechanic_id=?", nativeQuery = true)
    void removeMechanic(Long busId, Long mechanicId);


    @Modifying
    @Query(value = "UPDATE bus " +
            "SET driver_id=?1 " +
            "WHERE id=?2",
            nativeQuery = true)
    void setBusDriver(Long driverId, Long busId);

    @Modifying
    @Query(value = "UPDATE bus " +
            "SET route_id=?1 " +
            "WHERE id=?2",
            nativeQuery = true)
    void setRoute(Long routeId, Long busId);
}
