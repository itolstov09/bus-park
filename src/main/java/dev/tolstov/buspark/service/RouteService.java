package dev.tolstov.buspark.service;

import dev.tolstov.buspark.model.BusStop;
import dev.tolstov.buspark.model.Route;

import javax.validation.constraints.Positive;
import java.util.Set;

public interface RouteService extends BusParkBaseService<Route>{
      Route save(Route route);
      //todo используется только в тесте
      Route save(Route route, Set<BusStop> busStops);
      Route update(Long id, Route route);
      void addBusStop(@Positive Long busStopId, @Positive Long routeId);
      void removeBusStop(@Positive Long busStopId, @Positive Long routeId);
}
