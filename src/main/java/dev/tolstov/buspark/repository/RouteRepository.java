package dev.tolstov.buspark.repository;

import dev.tolstov.buspark.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RouteRepository extends JpaRepository<Route, Long> {

    boolean existsByRouteNumber(Integer routeNumber);

    @Query("SELECT route.id " +
            "FROM Route route " +
            "WHERE route.routeNumber=?1")
    Long getIdByRouteNumber(Integer routeNumber);
}
