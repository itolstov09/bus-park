package dev.tolstov.buspark.repository;

import dev.tolstov.buspark.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RouteRepository extends JpaRepository<Route, Long> {

    boolean existsByRouteNumber(Integer routeNumber);

    @Query("SELECT route.id " +
            "FROM Route route " +
            "WHERE route.routeNumber=?1")
    Long getIdByRouteNumber(Integer routeNumber);

    @Modifying
    @Query(value = "INSERT INTO route_bus_stop VALUES (?, ?)", nativeQuery = true)
    void addBusStop(Long routeId, Long busStopId);

    @Modifying
    @Query(value = "DELETE FROM route_bus_stop " +
            "WHERE route_id=? AND bus_stop_id=?", nativeQuery = true)
    void removeBusStop(Long routeId, Long busStopId);
}
