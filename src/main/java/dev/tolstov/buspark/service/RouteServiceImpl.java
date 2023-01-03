package dev.tolstov.buspark.service;

import dev.tolstov.buspark.exception.BPEntityNotFoundException;
import dev.tolstov.buspark.exception.BusStopException;
import dev.tolstov.buspark.model.BusStop;
import dev.tolstov.buspark.model.Route;
import dev.tolstov.buspark.repository.RouteRepository;
import dev.tolstov.buspark.validation.ValidationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class RouteServiceImpl implements RouteService {

    @Autowired
    RouteRepository routeRepository;

    @Autowired
    ValidationService validationService;

    @Autowired
    BusStopServiceImpl busStopServiceImpl;


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

        route.getBusStops().forEach(busStopServiceImpl::save);

        return routeRepository.save(route);
        }

    public Route save(Route newRoute, Set<BusStop> busStops) {
        newRoute.setBusStops(busStops);
        return routeRepository.save(newRoute);
    }

    @Override
    public List<Route> findAll() {
        return routeRepository.findAll();
    }

    @Override
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

    @Override
    public void deleteById(Long routeId) {
        Route byId = findById(routeId);
        routeRepository.delete(byId);
    }

    @Override
    public void deleteAll() {
        routeRepository.deleteAll();
    }

    @Override
    public Page<Route> getPage(@Min(0) Integer page, @Positive Integer size) {
        return routeRepository.findAll(PageRequest.of(page, size));
    }

    @Transactional
    public void addBusStop(@Positive Long busStopId, @Positive Long routeId) {
        checkExistById(routeId);
        if (!busStopServiceImpl.existById(busStopId)) {
            throw new BusStopException(
                    String.format("Cannot add bus stop. Bus stop with id %d not found", busStopId));
        }

        //todo костыль. тут либо надо в sql запросе проверять уникальность. или еще как то
        try {
            routeRepository.addBusStop(routeId, busStopId);
        } catch (DataIntegrityViolationException exception) {
            if (exception.getRootCause().getMessage().contains("route_bus_stop_pkey")) {
                throw new EntityExistsException("This route already have bus stop with id " + busStopId);
            }
        }
    }


    @Transactional
    public void removeBusStop(@Positive Long busStopId, @Positive Long routeId) {
        checkExistById(routeId);
//        routeRepository.
        routeRepository.removeBusStop(routeId, busStopId);
    }


    private void checkExistById(Long id) {
        if (!routeRepository.existsById(id)) {
            throw new BPEntityNotFoundException(Route.class.getSimpleName(), id);
        }
    }
}
