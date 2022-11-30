package dev.tolstov.buspark.service;

import dev.tolstov.buspark.exception.BPEntityNotFoundException;
import dev.tolstov.buspark.model.BusStop;
import dev.tolstov.buspark.model.Route;
import dev.tolstov.buspark.repository.RouteRepository;
import dev.tolstov.buspark.validation.ValidationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class RouteService {

    @Autowired
    RouteRepository routeRepository;

    @Autowired
    ValidationService validationService;

    @Autowired
    BusStopService busStopService;


    public Route save(Route route) {
        validationService.routeValidation(route);

        Integer routeNumber = route.getRouteNumber();
        Long idByRouteNumber = routeRepository.getIdByRouteNumber(routeNumber);
        Long routeId = route.getId();

        if ( routeId == null && routeRepository.existsByRouteNumber(routeNumber)
                || idByRouteNumber != null && !Objects.equals(idByRouteNumber, routeId)
        ) {
            throw new EntityExistsException(
                    String.format("Route with number %s already exists!", routeNumber)
            );
        }

        route.getBusStops().forEach(busStopService::save);

        return routeRepository.save(route);
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

    public Route update(Long id, Route routeInfo) {
        System.out.println("ROUTE UPDATE");
        Route byId = findById(id);
        BeanUtils.copyProperties(routeInfo, byId);
        return save(byId);
    }

    public void deleteById(Long routeId) {
        Route byId = findById(routeId);
        routeRepository.delete(byId);
    }

    public void deleteAll() {
        routeRepository.deleteAll();
    }

    public Page<Route> getPage(Integer page, Integer size) {
        return routeRepository.findAll(PageRequest.of(page, size));
    }
}
