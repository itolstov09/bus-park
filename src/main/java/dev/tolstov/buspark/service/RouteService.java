package dev.tolstov.buspark.service;

import dev.tolstov.buspark.exception.BPEntityNotFoundException;
import dev.tolstov.buspark.model.BusStop;
import dev.tolstov.buspark.model.Route;
import dev.tolstov.buspark.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RouteService {

    @Autowired
    RouteRepository routeRepository;


    public Route save(Route newRoute) {
        return routeRepository.save(newRoute);
    }

    public Route save(Route newRoute, Set<BusStop> busStops) {
        newRoute.setBusStops(busStops);
        return routeRepository.save(newRoute);
    }

    public List<Route> findAll() {
        return routeRepository.findAll();
    }

    public Route findById(Long routeId) {
        return routeRepository.findById(routeId)
                .orElseThrow(
                    () -> new BPEntityNotFoundException(
                            Route.class.getSimpleName(), routeId)
                );
    }

    public Route update(Route routeInfo) {
        return routeRepository.save(routeInfo);
    }

    public void deleteById(Long routeId) {
        routeRepository.deleteById(routeId);
    }

    public void deleteAll() {
        routeRepository.deleteAll();
    }
}
