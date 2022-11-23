package dev.tolstov.buspark.repository;

import dev.tolstov.buspark.model.Bus;
import dev.tolstov.buspark.model.Employee;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
