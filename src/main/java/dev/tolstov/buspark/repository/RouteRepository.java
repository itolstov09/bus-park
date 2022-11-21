package dev.tolstov.buspark.repository;

import dev.tolstov.buspark.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {

    boolean existsByRouteNumber(Integer routeNumber);
}
