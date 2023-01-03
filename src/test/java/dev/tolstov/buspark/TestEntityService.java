package dev.tolstov.buspark;

import dev.tolstov.buspark.model.Bus;
import dev.tolstov.buspark.model.Employee;
import dev.tolstov.buspark.model.Route;
import dev.tolstov.buspark.service.BusServiceImpl;
import dev.tolstov.buspark.service.EmployeeServiceImpl;
import dev.tolstov.buspark.service.RouteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Service
@Import(EntityTestConfig.class)
public class TestEntityService {
    @Autowired
    EmployeeServiceImpl employeeServiceImpl;

    @Autowired
    RouteServiceImpl routeServiceImpl;

    @Autowired
    BusServiceImpl busServiceImpl;


    @Transactional
    public void testFewRoutesCanHaveSameBusStop() {
        Route route = routeServiceImpl.findAll().get(1);

        Route newRoute = new Route(40);
        routeServiceImpl.save(newRoute, Set.of(route.getBusStops().iterator().next()));

        Route savedRoute = routeServiceImpl.findById(newRoute.getId());

        assertTrue(savedRoute.getBusStops().contains(
                route.getBusStops().iterator().next()
        ));
    }


    @Transactional
    public void testOneBusCanHaveFewMechanics() {
        Bus bus = busServiceImpl.findAll().get(0);
        assertTrue(bus.getMechanics().size() > 1);
    }

    public void testOneMechanicCanWorkOnFewBuses() {
        Employee mechanic = employeeServiceImpl.findFirstByPost(Employee.Post.MECHANIC);
        assertTrue(busServiceImpl.findBusesByMechanicId(mechanic.getId()).size() > 1);
    }
}
