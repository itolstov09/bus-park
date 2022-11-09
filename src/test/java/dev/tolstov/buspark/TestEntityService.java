package dev.tolstov.buspark;

import dev.tolstov.buspark.model.Route;
import dev.tolstov.buspark.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Service
@Import(EntityTestConfig.class)
public class TestEntityService {
    @Autowired
    RouteRepository routeRepository;


    @Transactional
    public void testFewRoutesCanHaveSameBusStop() {
        Route route = routeRepository.findAll().get(1);

        Route newRoute = new Route(40);
        newRoute.addBusStop(route.getBusStops().iterator().next());
        routeRepository.save(newRoute);

        Route savedRoute = routeRepository.findById(newRoute.getId()).get();

        assertTrue(savedRoute.getBusStops().contains(
                route.getBusStops().iterator().next()
        ));
    }
}
