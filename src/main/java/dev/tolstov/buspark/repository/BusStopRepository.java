package dev.tolstov.buspark.repository;

import dev.tolstov.buspark.model.BusStop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusStopRepository extends JpaRepository<BusStop, Long> {
}
