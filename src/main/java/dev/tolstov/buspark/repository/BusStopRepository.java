package dev.tolstov.buspark.repository;

import dev.tolstov.buspark.model.BusStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BusStopRepository extends JpaRepository<BusStop, Long> {
    boolean existsByName(String name);

    @Query("SELECT busStop.id " +
            "FROM BusStop busStop " +
            "WHERE busStop.name=?1")
    Long getIdByName(String name);

}
